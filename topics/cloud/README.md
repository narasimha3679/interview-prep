# Cloud for SDETs — Study Pack

Cloud fundamentals and the testing implications of managed infrastructure. **Azure-first**, with a
provider mapping so the concepts transfer to AWS and GCP.

**Last verified:** November 2026.

---

## 60-second cram

1. **IaaS vs PaaS vs SaaS** — you manage the stack at different levels. For test environments,
   PaaS is usually the best trade-off: less undifferentiated ops, still realistic.
2. **Regions and availability zones** — a region is a geography; an AZ is an isolated datacenter
   group inside it. Multi-AZ is resilience; multi-region is disaster recovery.
3. **Shared responsibility** — the provider secures the cloud; you secure what you put in it.
   Identity, config, data, and access are yours.
4. **Managed identity beats secrets** — give the workload an identity, grant it the minimum role,
   and let the platform issue short-lived credentials.
5. **Ephemeral environments** — provision per PR or per test run with IaC, seed, test, destroy.
   This is the cloud version of Testcontainers.
6. **Deployment slots/revisions** — deploy to a non-production slot, run smoke, then swap or shift
   traffic. Rollback is a traffic switch.
7. **Cloud testing is not local testing** — network latency, quotas, managed-service limits, region
   behavior, IAM, and cost are part of the test surface.
8. **Cost is a test-environment concern** — budgets, tags, auto-shutdown, and cleanup are part of
   the design, not an afterthought.

**The one line that shows depth:**

> "The cloud doesn't remove infrastructure testing; it moves the risk into identity, configuration,
> network boundaries, managed-service behavior, and cost — so those become explicit test targets."

---

## Deployment models

| Model | What it is | Test implication |
|---|---|---|
| **Public cloud** | Provider-owned, multi-tenant | Quotas, region availability, shared responsibility |
| **Private cloud** | Dedicated to one organization | More control, more ops burden |
| **Hybrid** | Mix of on-prem and cloud | Network, identity, and data consistency across boundaries |
| **Multi-cloud** | More than one provider | Portability, provider-specific behavior, duplicated controls |

## Service models

| Model | You manage | Provider manages | Example |
|---|---|---|---|
| **IaaS** | OS, runtime, app, data | Virtualization, network, storage | Azure VMs, AWS EC2, GCP Compute Engine |
| **PaaS** | App, data, config | OS, runtime, scaling, patching | Azure App Service, AWS Elastic Beanstalk, GCP App Engine |
| **SaaS** | Your data and access | Everything else | Microsoft 365, Salesforce, GitHub |

**Testing rule:** the higher the managed layer, the less you test the platform and the more you
test **configuration, integration, identity, and behavior**.

---

## Core concepts

| Concept | Meaning | Testing implication |
|---|---|---|
| **Region** | Geographic area with datacenters | Data residency, latency, service availability |
| **Availability zone** | Isolated datacenter group in a region | Multi-AZ failover, zone-aware test data |
| **Region pair** | Paired regions for replication/recovery | DR tests, failover direction |
| **SLA** | Provider's availability commitment | Composite SLA across dependencies |
| **RTO/RPO** | Recovery time / recovery point objectives | Backup and failover tests |
| **Scalability** | Ability to handle growth | Load tests, autoscaling behavior |
| **Elasticity** | Automatic scale up/down | Scale-in/out correctness, cold starts |
| **Managed identity** | Platform-issued workload identity | Auth tests without stored secrets |
| **IaC** | Infrastructure as code | Reproducible, reviewable test environments |
| **Resource group** | Logical container for Azure resources | Per-PR isolation and cleanup |
| **Tag** | Metadata on resources | Cost attribution, ownership, cleanup |
| **Quota** | Provider limit per subscription/region | Test at the limit; plan capacity |

---

## Shared responsibility model

| Layer | Provider | You |
|---|---|---|
| Physical datacenters | ✅ | — |
| Network backbone | ✅ | — |
| Hypervisor | ✅ | — |
| Operating system (IaaS) | — | ✅ |
| Runtime (PaaS) | ✅ | — |
| Application code | — | ✅ |
| Identity and access | — | ✅ |
| Data classification | — | ✅ |
| Configuration | — | ✅ |
| Secrets | — | ✅ |
| Monitoring and alerts | — | ✅ |

**Interview line:** "The provider guarantees the cloud is available; they do not guarantee my
configuration, identity model, or data handling are correct. That's where most cloud incidents
come from, and that's what I test."

---

## Cloud testing checklist

- [ ] Infrastructure is provisioned from code, not clicked together.
- [ ] Test environments are isolated (subscription/resource group/namespace).
- [ ] Cleanup is automatic and verified.
- [ ] Cost is bounded by budgets, tags, and auto-shutdown.
- [ ] Identity uses managed identity or short-lived credentials.
- [ ] Secrets are in a managed secret store, not config files.
- [ ] Network boundaries (private endpoints, NSGs, firewalls) are tested.
- [ ] Quotas and limits are known and tested.
- [ ] Managed-service behavior is tested, including throttling and retries.
- [ ] Region/AZ failover is tested where the SLA requires it.
- [ ] Observability is wired before the first deploy.
- [ ] The same artifact is promoted across environments.

---

## File map

| File | What it gives you |
|---|---|
| [`aws-gcp-mapping.md`](aws-gcp-mapping.md) | Azure ↔ AWS ↔ GCP equivalents |
| [`containers/01_docker.md`](containers/01_docker.md) | Docker, images, Compose, Testcontainers |
| [`containers/02_kubernetes.md`](containers/02_kubernetes.md) | Kubernetes objects and testing implications |
| [`azure/01_az900_refresh.md`](azure/01_az900_refresh.md) | AZ-900 cram sheet |
| [`azure/02_azure_core.md`](azure/02_azure_core.md) | Azure services by category |
| [`azure/03_azure_for_sdet.md`](azure/03_azure_for_sdet.md) | Test environments, deployment validation, identity, async |
| [`azure/04_azure_devops_cicd.md`](azure/04_azure_devops_cicd.md) | Azure Pipelines, GitHub Actions, slots, canary |
| [`azure/05_azure_qa.md`](azure/05_azure_qa.md) | Questions with model answers |
| [`azure/examples/`](azure/examples/) | Bicep/OIDC reference examples |

---

## How to study this pack

- **10 minutes:** README + cram sheet.
- **30 minutes:** Azure core services, then draw the Azure hierarchy and a test environment.
- **30 minutes:** Azure for SDETs; write a release-validation checklist for a service.
- **30 minutes:** Docker/Kubernetes; explain how an ephemeral environment works.
- **30 minutes:** Q&A out loud.
