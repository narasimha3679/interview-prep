# 03 — Azure for SDETs

How to build, validate, and debug test environments on Azure. This is the interview-relevant part:
not "what is Azure," but "what would you do with it."

---

## 1. Reference test environment

```text
Resource group: rg-listings-pr-1234
├── App Service (API)                 -> staging slot
├── Azure SQL Database                 -> seeded per run
├── Service Bus namespace              -> test queue/topic
├── Key Vault                          -> test secrets
├── Application Insights               -> traces, failures
├── Log Analytics workspace            -> KQL queries
├── Storage account                    -> payloads, reports
└── Managed identity                   -> access to SQL/Key Vault/Storage
```

Design rules:

- One resource group per PR or test run.
- Everything named with the PR/environment ID.
- All resources tagged: `env`, `owner`, `pr`, `expires`.
- Managed identity for service-to-service auth.
- Secrets from Key Vault, not pipeline variables baked into config.
- Delete the resource group on merge/close.
- Budget alert on the subscription/resource group.

---

## 2. Provisioning test environments

### Option A — Bicep/ARM

```bicep
param env string
param location string = resourceGroup().location

resource app 'Microsoft.Web/sites@2023-12-01' = {
  name: 'app-listings-${env}'
  location: location
  kind: 'app,linux'
  properties: {
    serverFarmId: plan.id
    siteConfig: {
      linuxFxVersion: 'JAVA|17-java17'
      appSettings: [
        { name: 'ENV', value: env }
        { name: 'KEY_VAULT_URI', value: kv.properties.vaultUri }
      ]
    }
  }
  identity: { type: 'SystemAssigned' }
}
```

### Option B — Terraform

```hcl
resource "azurerm_resource_group" "test" {
  name     = "rg-listings-${var.pr}"
  location = var.location
  tags = {
    env     = "test"
    pr      = var.pr
    expires = var.expires
  }
}
```

### Option C — Azure Deployment Environments / DevTest Labs

Use when you want self-service environments with templates, quotas, and auto-shutdown. Good for
teams that don't want every QE engineer writing Terraform.

**Testing rule:** the environment definition is code, reviewed like code, and versioned with the
application.

---

## 3. Deployment validation

### App Service slots

```bash
# Deploy to staging slot
az webapp deployment slot create --name app-listings --resource-group rg-test --slot staging
az webapp deploy --name app-listings --resource-group rg-test --slot staging --src-path app.jar

# Warm up and smoke test the slot
curl -f https://app-listings-staging.azurewebsites.net/health

# Swap only after smoke passes
az webapp deployment slot swap --name app-listings --resource-group rg-test \
  --slot staging --target-slot production

# Rollback: swap back
az webapp deployment slot swap --name app-listings --resource-group rg-test \
  --slot production --target-slot staging
```

Test focus:

- Staging slot uses production-like config (or a separate config slot setting).
- Warm-up before swap; cold start can look like a failure.
- Sticky settings: connection strings and slot-specific config do not accidentally swap.
- Smoke test the slot URL, then the production URL after swap.
- Rollback is a swap, not a redeploy.

### Container Apps revisions

```bash
az containerapp revision list --name listings --resource-group rg-test -o table
az containerapp ingress traffic set --name listings --resource-group rg-test \
  --revision-weight latest=10
```

Test focus:

- New revision becomes healthy before traffic shifts.
- Canary traffic split; monitor error rate/latency.
- Rollback by shifting weight to the previous revision.
- Scale-to-zero cold start behavior.

### AKS

```bash
kubectl apply -f k8s/ -n test-pr-1234
kubectl rollout status deployment/listings -n test-pr-1234
kubectl rollout undo deployment/listings -n test-pr-1234
```

Test focus:

- Readiness gates traffic.
- Rolling update respects `maxUnavailable`.
- Previous version remains compatible with the database.
- Namespace cleanup removes all resources.

---

## 4. Identity and secrets in tests

### Managed identity

```java
// DefaultAzureCredential picks up managed identity in Azure,
// environment credentials locally, and CLI credentials for developers.
DefaultAzureCredential credential = new DefaultAzureCredential();
TokenRequestContext context = new TokenRequestContext(
        new String[] { "https://vault.azure.net/.default" });
AccessToken token = credential.getToken(context).block();
```

Test cases:

- Workload can acquire a token without a stored secret.
- Workload has the minimum role, not Owner/Contributor.
- Access to a resource is denied when the identity is removed.
- Token expiry/refresh does not break long-running tests.

### Key Vault

- Secrets are read at runtime, not baked into the image.
- Access via RBAC or access policies, not account keys.
- Private endpoint blocks public access.
- Secret rotation does not require a redeploy.

### RBAC/Policy

- A user with Reader cannot write.
- A resource outside allowed regions is denied by Policy.
- A resource without required tags is denied or flagged.
- Resource locks prevent deletion.

---

## 5. Async and messaging tests

### Service Bus

```java
// Send
sender.sendMessage(new ServiceBusMessage(payload)
        .setMessageId(id)
        .setContentType("application/json"));

// Receive with a timeout
ServiceBusReceiver receiver = client.createReceiver(queueName);
ServiceBusReceivedMessage message = receiver.receiveMessage(Duration.ofSeconds(10));
assertThat(message.getBody().toString()).contains(id);
```

Test cases:

- Message is delivered exactly once or idempotently.
- Duplicate message does not double-apply.
- Dead-letter queue receives poison messages.
- Ordering is preserved when required (sessions).
- Retry/backoff behavior.
- Message schema is compatible with consumers.

### Event Grid / Event Hubs

- Event Grid: filters, retries, dead-letter, delivery schema.
- Event Hubs: partitions, consumer groups, offsets, replay.
- Always poll to eventual state; do not assert immediately.

**Test pattern:**

```text
act (API call)
  -> poll for event/state with timeout
  -> assert payload schema
  -> assert idempotency with duplicate
  -> assert dead-letter empty
```

---

## 6. Observability for test failures

| Tool | Use in a test failure |
|---|---|
| **Application Insights** | Find the failed request, dependency call, exception, trace |
| **Log Analytics (KQL)** | Query logs by correlation ID, operation ID, or test run ID |
| **Azure Monitor alerts** | Detect error-rate/latency regressions during canary |
| **Availability tests** | Synthetic checks from multiple regions |
| **Service Health** | Rule out a platform incident |

**Pass a correlation ID from the test:**

```java
given()
    .header("x-correlation-id", testRunId)
.when()
    .get("/listings/123")
.then()
    .statusCode(200);
```

Then query:

```kusto
traces
| where customDimensions.correlationId == "test-run-123"
| order by timestamp asc
```

**Interview line:** "A cloud test failure should produce a trace ID, not just a stack trace. I pass
a correlation ID from the test and use Application Insights/Log Analytics to find the service that
actually failed."

---

## 7. Local emulators vs real cloud

| Dependency | Local option | Limits |
|---|---|---|
| Blob/Queue/Table | Azurite | Not all features; no private endpoint |
| Cosmos DB | Cosmos DB emulator | Windows/Linux; not all APIs/consistency behavior |
| Functions | Azure Functions Core Tools | No full platform behavior |
| Service Bus | Service Bus emulator (preview) | Feature subset |
| Event Hubs | Event Hubs emulator | Feature subset |
| SQL | SQL Server container / H2 | H2 is not SQL Server; use real SQL for dialect |
| Key Vault | Mock/secrets in env | No managed identity/private endpoint |

**Rule:** emulators for the fast inner loop; real Azure for integration and release validation.
Do not claim a private-endpoint or managed-identity test passed because it passed against an
emulator.

---

## 8. Cost control for test environments

- One resource group per PR/test run, deleted automatically.
- Auto-shutdown for VMs and dev/test labs.
- Scale-to-zero for Container Apps/Functions where possible.
- Free tiers for dev/test subscriptions.
- Budgets and alerts on the subscription/resource group.
- Tags for cost attribution.
- Avoid premium tiers unless the test requires them.
- Clean up public IPs, load balancers, disks, and snapshots.
- Use spot VMs for interruptible test agents.

**Interview line:** "A test environment that nobody owns becomes a cost leak. I'd make expiry and
cleanup part of the environment template, not a manual chore."

---

## 9. Release validation on Azure

```text
1. Deploy artifact to staging slot / new revision
2. Wait for health/readiness
3. Run smoke + critical API + auth + migration checks
4. Check Application Insights for new exceptions/dependency failures
5. Shift 5–10% traffic to canary
6. Monitor error rate, latency, and business metric for N minutes
7. Promote to 100% or swap slot
8. Run post-deploy smoke
9. Rollback by slot swap / revision weight / previous artifact
```

Validation set:

- Health/readiness
- Login/token refresh
- One read and one write per critical API
- One async publish/consume path
- Database migration applied
- Key Vault/identity path works
- No new exceptions in App Insights
- Canary metrics within threshold

---

## 10. Azure testing checklist

- [ ] Environment is provisioned from code.
- [ ] Resource group is isolated and tagged.
- [ ] Managed identity is used; no stored credentials.
- [ ] Secrets come from Key Vault.
- [ ] Private endpoints/NSGs are tested for both allow and deny.
- [ ] Deployment slot/revision is validated before traffic shift.
- [ ] Rollback path is tested.
- [ ] Async flows poll to eventual state.
- [ ] Correlation IDs flow into App Insights/Log Analytics.
- [ ] Budget and cleanup are configured.
- [ ] Emulator limitations are documented.
- [ ] Release validation runs against the deployed artifact, not localhost.
