# 05 — Azure Q&A

Cover the answer, say your version out loud, then compare.

---

## A. Cloud fundamentals

**A1. What is cloud computing?**
> "Renting compute, storage, networking, and managed services from a provider on a consumption
> model. The value is elasticity, global reach, managed operations, and shifting spend from CapEx to
> OpEx. The cost is that identity, configuration, network boundaries, and cost control become your
> responsibility."

**A2. IaaS vs PaaS vs SaaS?**
> "IaaS gives you virtual machines and you manage the OS and runtime. PaaS gives you a managed
> runtime — you bring the app and config. SaaS gives you the whole application. For test
> environments I usually prefer PaaS: less patching and setup, still realistic enough to test the
> app and its integrations."

**A3. Public vs private vs hybrid?**
> "Public is provider-owned multi-tenant infrastructure. Private is dedicated to one organization.
> Hybrid connects both, usually for data residency, legacy systems, or gradual migration. Hybrid is
> the hardest to test because identity, network, and data consistency cross the boundary."

**A4. What is the shared responsibility model?**
> "The provider secures the physical infrastructure, hypervisor, and managed platform. I secure
> identity, access, configuration, data, secrets, and monitoring. Most cloud incidents are
> configuration or identity failures on the customer side, not provider outages."

**A5. What is an availability zone?**
> "A physically separate group of datacenters within a region, with independent power, cooling, and
> networking. Multi-AZ protects against a datacenter failure. Multi-region protects against a
> regional disaster. They solve different risks."

**A6. What is a region pair?**
> "Two regions paired by the provider for replication and recovery, usually in the same geography.
> It gives a preferred recovery direction and helps with data residency. It is not the same as an
> availability zone."

**A7. What is an SLA? What is a composite SLA?**
> "An SLA is the provider's availability commitment. A composite SLA multiplies the SLAs of the
> dependencies in your path, so more dependencies mean lower end-to-end availability. Two 99.9%
> services combine to about 99.8%."

**A8. Scalability vs elasticity?**
> "Scalability is the ability to handle growth by adding capacity. Elasticity is automatic scaling
> up and down with demand. Elasticity is a cost and correctness concern: scale-in can kill
> in-flight work, and cold starts can look like failures."

---

## B. Azure core

**B1. Explain the Azure hierarchy.**
> "Tenant → management groups → subscriptions → resource groups → resources. Management groups
> apply policy and RBAC across subscriptions; subscriptions are billing/quota boundaries; resource
> groups are lifecycle boundaries. I use one resource group per test environment so cleanup is one
> delete."

**B2. App Service vs Container Apps vs AKS vs Functions?**
> "App Service for a web app/API with minimal ops. Container Apps for a container with serverless
> scaling and revisions, without Kubernetes. AKS when I need Kubernetes control, custom networking,
> or a large service estate. Functions for event-driven, short-lived work."

**B3. When would you use a VM instead of PaaS?**
> "When I need full OS control, a legacy app, custom agents, or software that the PaaS doesn't
> support. The trade-off is patching, scaling, and security ownership."

**B4. What is the difference between Blob, Files, Disk, Queue, and Table?**
> "Blob is object storage, Files is SMB/NFS shares, Disk is block storage for VMs, Queue is simple
> messaging, and Table is key-value NoSQL. For test artifacts and payloads, Blob is the usual
> choice."

**B5. What are LRS, ZRS, and GRS?**
> "LRS keeps three copies in one datacenter. ZRS spreads copies across availability zones in one
> region. GRS replicates to a paired region. RA-GRS adds read access to the secondary. The choice
> is an RTO/RPO and cost decision."

**B6. NSG vs Azure Firewall vs Application Gateway?**
> "NSG filters traffic at the subnet/NIC level by port/protocol/source. Azure Firewall is a managed
> network security service with centralized policy and FQDN filtering. Application Gateway is a
> layer-7 load balancer with WAF and TLS termination. They solve different layers."

**B7. What is a private endpoint?**
> "A private IP in my VNet that connects to a PaaS service, so traffic doesn't go over the public
> internet. I test both that the private path works and that the public path is blocked."

**B8. What is Azure Policy vs RBAC?**
> "RBAC controls who can perform actions on resources. Azure Policy controls what resources are
> allowed to exist and how they're configured — allowed regions, required tags, SKUs, diagnostic
> settings."

**B9. What is a managed identity vs a service principal?**
> "A managed identity is a workload identity managed by Azure; the credential is created and
> rotated for me. A service principal is an app registration where I manage the credential. I prefer
> managed identity inside Azure and workload identity federation for external CI."

**B10. What is Key Vault?**
> "A managed store for secrets, keys, and certificates. I access it with managed identity/RBAC,
> keep secrets out of code and pipeline logs, and use private endpoints where required."

---

## C. Azure for testing

**C1. How would you provision a test environment in Azure?**
> "From code — Bicep or Terraform — into an isolated resource group per PR, tagged with owner and
> expiry. The template includes the app, database, messaging, Key Vault, monitoring, and managed
> identity. Seed data through APIs, run the suite, publish artifacts, then delete the resource
> group."

**C2. How do you validate a deployment before production?**
> "Deploy the same artifact to a staging slot or new Container Apps revision, wait for readiness,
> run smoke plus critical API/auth/migration checks, check Application Insights for new exceptions,
> then shift canary traffic and monitor error rate/latency before full promotion. Rollback is a slot
> swap or revision weight change."

**C3. How do deployment slots help?**
> "They give me a warm, separately addressable version of the app. I deploy to staging, warm it up,
> run smoke, then swap. Rollback is swapping back. The key detail is slot settings: secrets and
> connection strings must be marked so they don't swap."

**C4. How do you test async messaging on Azure?**
> "Send the message or trigger the API, poll for the eventual state with a timeout, assert the
> payload schema, test duplicate delivery for idempotency, and verify the dead-letter queue is
> empty. Service Bus, Event Grid, and Event Hubs have different delivery semantics, so the test
> shape changes."

**C5. How do you debug a test that only fails in Azure?**
> "Get the correlation ID from the test, query Application Insights and Log Analytics for the
> request/dependency/exception, check Service Health for a platform incident, and reproduce in a
> staging slot or ephemeral environment. Most of the time it's config, identity, network, or a
> managed-service limit."

**C6. How do you test identity and RBAC?**
> "Positive: the workload can acquire a token and access the resource. Negative: removing the role
> or using the wrong identity is denied. Also test least privilege — the identity should not be
> able to read/write unrelated resource groups."

**C7. How do you control test-environment cost?**
> "One resource group per run with expiry, auto-shutdown, scale-to-zero, budgets/alerts, tags, and
> automatic deletion. I avoid premium tiers unless the test requires them and clean up public IPs,
> disks, and load balancers."

**C8. What can't you test locally?**
> "Managed identity, private endpoints, RBAC, region failover, quotas, managed-service throttling,
> and real network latency. Emulators are useful for the fast loop, but release validation must run
> in Azure."

**C9. How do you test a microservice on AKS?**
> "Deploy into an isolated namespace, wait for readiness, test service-to-service calls through
> Kubernetes DNS, test ingress/TLS/auth, verify network policies, run a rolling update and rollback,
> and collect logs/events on failure."

**C10. How do you test serverless functions?**
> "Unit-test the handler; integration-test triggers and bindings with the Functions runtime or a
> local emulator; test cold start, retries, poison messages, idempotency, and timeout behavior in a
> real environment."

---

## D. CI/CD and release

**D1. Azure Pipelines vs GitHub Actions?**
> "Both can build, test, and deploy. Azure Pipelines has deep Azure integration, environments with
> approvals, and variable groups/Key Vault. GitHub Actions is simpler for GitHub repos and has a
> huge action ecosystem. I'd choose based on where the code lives and the organization's governance,
> not on YAML preference."

**D2. How do you authenticate to Azure from CI?**
> "Workload identity federation/OIDC: the pipeline gets a short-lived token trusted by an app
> registration or managed identity. No long-lived client secret. Scope the identity to the target
> resource group and minimum role."

**D3. What gates would you put before production?**
> "Contract can-i-deploy, smoke pass, release validation on staging, migration check, no new
> critical security findings, canary metrics within budget, and a verified rollback. Manual approval
> only for the business decision or irreversible step."

**D4. How do you roll back?**
> "App Service: swap back. Container Apps: shift traffic to the previous revision. AKS: rollout
> undo or GitOps revert. The database must remain compatible with the previous version, so I use
> expand/contract migrations."

**D5. How do you keep the pipeline fast?**
> "Cheap tests first, parallel independent jobs, dependency caching, test sharding, and moving slow
> regression to nightly. I measure p95 duration and queue time, not just the average."

**D6. How do you handle flaky tests in CI?**
> "Track flaky rate per test, quarantine with an owner and deadline, fix root causes, and report
> retried-but-passed separately. Retries are a detector, not a fix."

---

## E. Scenario questions

**E1. A release passes staging but fails in production. What do you check first?**
> "Config and secrets, identity/RBAC, network/private endpoints, DNS/TLS, database migration,
> feature flags, and version mismatch. Then compare the deployed artifact digest and the
> environment-specific settings."

**E2. A test works locally but times out in Azure. What do you do?**
> "Check cold start, network latency, managed-service throttling, connection pool limits, and
> timeout values. Add a correlation ID, inspect dependency durations in Application Insights, and
> reproduce in staging before changing the test."

**E3. Your team wants to run all E2E tests on every PR. How do you respond?**
> "I'd show the cost: pipeline duration, flakiness, and queue time. Then propose a split — fast
> API/component/contract on PR, UI smoke on merge, full E2E nightly and pre-release. The goal is
> the fastest signal for the risk being changed, not maximum test count."

**E4. How would you test a new Azure integration?**
> "Start with the contract and identity: who calls whom, with what permissions. Then test the happy
> path, auth failure, timeout/retry, throttling, malformed payload, duplicate delivery, and
> observability. Use an emulator for the fast loop and a real environment for release validation."

**E5. What would you monitor after a release?**
> "Error rate, latency p95/p99, saturation, dependency failures, queue depth/dead-letter count,
> and the business metric for the changed flow. Plus synthetic checks on the critical journey."
