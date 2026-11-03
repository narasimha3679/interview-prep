# 02 — Azure Core Services

A working map of Azure services grouped by what an SDET actually needs to know.

---

## 1. Compute

| Service | Model | Use | Test focus |
|---|---|---|---|
| **Virtual Machines** | IaaS | Full OS control, legacy apps, custom agents | Boot, extensions, disks, network, patching |
| **App Service** | PaaS | Web apps and APIs | Slots, config, scaling, auth, deployment |
| **Container Apps** | PaaS containers | Containerized APIs, jobs, scale-to-zero | Revisions, traffic split, cold start |
| **AKS** | Kubernetes | Complex microservices, custom networking | Rollout, readiness, namespaces, policies |
| **Functions** | Serverless | Event-driven, short-lived work | Triggers, bindings, cold start, retries |
| **Batch** | Managed batch | Large parallel compute | Job scheduling, retries, cleanup |

**App Service vs Container Apps vs AKS vs Functions:**

- App Service: you have a web app/API and want minimal ops.
- Container Apps: you have a container and want serverless scaling without Kubernetes.
- AKS: you need Kubernetes control, custom networking, or a large service estate.
- Functions: event-driven, short-lived, pay-per-execution.

---

## 2. Storage

| Service | Use | Test focus |
|---|---|---|
| **Blob Storage** | Objects: images, documents, backups | Auth, SAS, tiers, lifecycle, private endpoint |
| **Azure Files** | SMB/NFS shares | Mount, permissions, latency |
| **Managed Disks** | VM block storage | Performance tiers, snapshots, encryption |
| **Queue Storage** | Simple messages | Visibility timeout, poison messages, idempotency |
| **Table Storage** | Key-value NoSQL | Partition/row keys, throughput |

**Blob access options:** managed identity (best), SAS token (scoped/time-bound), account key (avoid).

**Redundancy:** LRS, ZRS, GRS, RA-GRS, GZRS. Choose based on RTO/RPO and cost.

---

## 3. Networking

| Service | Layer | Use | Test focus |
|---|---|---|---|
| **VNet** | 3 | Network isolation | Address space, peering, DNS |
| **Subnet** | 3 | Segmentation | Delegation, service endpoints |
| **NSG** | 3/4 | Port/protocol rules | Allowed/denied paths, priority |
| **Azure Firewall** | 3/4 | Managed firewall | Egress rules, FQDN filtering |
| **Load Balancer** | 4 | TCP/UDP distribution | Health probes, session persistence |
| **Application Gateway** | 7 | HTTP routing, WAF, TLS | Path routing, WAF rules, TLS |
| **Front Door** | 7 | Global HTTP entry, CDN, WAF | Global failover, caching, WAF |
| **Private Endpoint** | — | Private PaaS access | DNS resolution, no public path |
| **Azure DNS** | — | DNS hosting | Record correctness, TTL, failover |

**Testing rule:** if a service is private, test that the public path is actually blocked, not just
that the private path works.

---

## 4. Data

| Service | Type | Use | Test focus |
|---|---|---|---|
| **Azure SQL Database** | Relational | Managed SQL Server | Schema, migrations, failover, DTU/vCore |
| **Azure Database for PostgreSQL/MySQL** | Relational | Managed open-source DB | Extensions, replication, connection limits |
| **Cosmos DB** | Multi-model NoSQL | Global distribution, low latency | Consistency levels, partition keys, RU throttling |
| **Azure Cache for Redis** | Cache | Low-latency cache | Eviction, TTL, failover |
| **Azure Synapse** | Analytics | Data warehouse | Data quality, query performance |
| **Data Factory** | ETL/ELT | Data pipelines | Data lineage, retries, idempotency |

**Cosmos DB consistency levels:** strong, bounded staleness, session, consistent prefix, eventual.
The level changes what a test can assert immediately.

---

## 5. Messaging and integration

| Service | Pattern | Use | Test focus |
|---|---|---|---|
| **Service Bus** | Queue/topic | Reliable enterprise messaging | Sessions, dead-letter, duplicate detection, ordering |
| **Event Grid** | Event routing | Reactive event delivery | Filters, retries, dead-letter |
| **Event Hubs** | Event streaming | High-throughput telemetry/streams | Partitions, consumer groups, offsets |
| **API Management** | API gateway | Publish/manage APIs | Policies, rate limits, auth, mocking |
| **Logic Apps** | Workflow | Integration orchestration | Triggers, actions, retries |

**Service Bus vs Event Grid vs Event Hubs:**

- Service Bus: commands/events that must be processed reliably, with queues/topics.
- Event Grid: lightweight notifications about state changes.
- Event Hubs: high-volume streaming telemetry.

---

## 6. Identity and security

| Service | Purpose | Test focus |
|---|---|---|
| **Microsoft Entra ID** | Identity provider | Login, MFA, conditional access, groups |
| **RBAC** | Authorization | Least privilege, role assignments, deny assignments |
| **Managed Identity** | Workload identity | Token acquisition, no stored secrets |
| **Service Principal** | App identity | Credential rotation, scopes |
| **Key Vault** | Secrets/keys/certs | Access policies vs RBAC, rotation, private endpoint |
| **Defender for Cloud** | Security posture | Findings, recommendations, alerts |
| **Azure Policy** | Resource rules | Allowed regions/SKUs, required tags, deny effects |
| **Resource Locks** | Protect resources | CanNotDelete, ReadOnly |

**Managed identity vs service principal:**

- Managed identity: Azure creates and rotates the credential; no secret to store.
- Service principal: you create an app registration and manage its credential/certificate.

**Prefer managed identity** for workloads running in Azure. Use workload identity federation for
CI systems outside Azure.

---

## 7. Monitoring and operations

| Service | Use |
|---|---|
| **Azure Monitor** | Metrics, alerts, dashboards, autoscale |
| **Application Insights** | APM: requests, dependencies, traces, exceptions, availability tests |
| **Log Analytics** | Query logs with KQL |
| **Azure Advisor** | Cost, security, reliability, performance recommendations |
| **Service Health** | Platform incidents and maintenance |
| **Azure Status** | Global service status |

**Testing rule:** wire Application Insights and Log Analytics **before** the first release. A test
failure without a trace or log is a debugging tax.

---

## 8. DevOps

| Service | Use |
|---|---|
| **Azure Repos** | Git repositories |
| **Azure Pipelines** | CI/CD |
| **Azure Boards** | Work tracking |
| **Azure Artifacts** | Package feeds (Maven, npm, NuGet) |
| **Azure Test Plans** | Manual/exploratory test management |
| **Azure Container Registry** | Private container images |

See [`04_azure_devops_cicd.md`](04_azure_devops_cicd.md).

---

## 9. Governance and cost

| Tool | Use |
|---|---|
| **Tags** | Environment, owner, cost center |
| **Budgets** | Alerts and spend limits |
| **Cost Analysis** | Break down spend by resource/group/tag |
| **Azure Policy** | Enforce standards |
| **Management Groups** | Policy/RBAC at scale |
| **Resource Locks** | Prevent accidental deletion |
| **Azure Advisor** | Optimization recommendations |

**Test environment cost controls:**

- Auto-shutdown schedules for VMs.
- Scale-to-zero for Container Apps/Functions.
- Delete resource groups after test runs.
- Use free tiers and dev/test pricing where eligible.
- Tag everything and alert on budget.
- Avoid leaving public IPs, load balancers, and premium disks running.

---

## 10. Service selection cheat sheet

| Need | Start with |
|---|---|
| Simple web API | App Service |
| Containerized API without K8s | Container Apps |
| Full Kubernetes | AKS |
| Event-driven function | Functions |
| Relational data | Azure SQL / PostgreSQL |
| Global NoSQL | Cosmos DB |
| Object storage | Blob Storage |
| Reliable queue/topic | Service Bus |
| High-volume stream | Event Hubs |
| Lightweight events | Event Grid |
| Secrets | Key Vault |
| API gateway | API Management |
| Monitoring | Application Insights + Log Analytics |
| Private PaaS access | Private Endpoint |
