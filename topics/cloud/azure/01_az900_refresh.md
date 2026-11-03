# 01 — AZ-900 Refresh

A one-page refresher. You already have the certificate; this is to keep the vocabulary sharp for
conversations, not to re-take the exam.

---

## 1. Cloud models

| Model | Description | Example |
|---|---|---|
| **Public** | Provider-owned, shared infrastructure | Azure, AWS, GCP |
| **Private** | Dedicated to one organization | On-prem Azure Stack, private cloud |
| **Hybrid** | Mix of public and private | On-prem DB + cloud app |
| **Multi-cloud** | More than one provider | Azure + AWS |

**Consumption-based pricing:** pay for what you use; CapEx becomes OpEx. Benefits: no upfront
capacity, elasticity, economies of scale.

---

## 2. Benefits of cloud

- **High availability** — service remains available with minimal downtime.
- **Scalability** — vertical (more CPU/RAM) or horizontal (more instances).
- **Elasticity** — automatic scale based on demand.
- **Reliability** — recover from failures; designed for resilience.
- **Predictability** — consistent performance and cost.
- **Security** — provider controls plus your configuration.
- **Governance** — policy, compliance, and cost controls.
- **Manageability** — monitoring, automation, IaC.

---

## 3. Azure hierarchy

```text
Tenant (Microsoft Entra ID)
└── Management groups
    └── Subscriptions
        └── Resource groups
            └── Resources
```

| Level | Purpose |
|---|---|
| **Tenant** | Identity boundary; Entra ID directory |
| **Management group** | Organize subscriptions; apply policy/RBAC at scale |
| **Subscription** | Billing and quota boundary |
| **Resource group** | Logical container for related resources; lifecycle boundary |
| **Resource** | A single service instance (VM, SQL DB, storage account) |

**Testing rule:** use a resource group per environment/PR so cleanup is one delete.

---

## 4. Regions and availability

| Concept | Meaning |
|---|---|
| **Region** | Geographic area with datacenters |
| **Availability zone (AZ)** | Physically separate datacenter group in a region |
| **Region pair** | Two regions paired for replication and recovery |
| **Sovereign region** | Isolated region for compliance (e.g., government) |

- **Multi-AZ** protects against a datacenter failure.
- **Multi-region** protects against a regional disaster.
- **Region pair** gives a preferred recovery direction.

---

## 5. Core service categories

| Category | Azure services |
|---|---|
| **Compute** | VMs, App Service, Container Apps, AKS, Functions |
| **Storage** | Blob, Files, Disk, Queue, Table |
| **Networking** | VNet, Subnet, NSG, Load Balancer, Application Gateway, Front Door, DNS |
| **Databases** | Azure SQL, Cosmos DB, PostgreSQL, MySQL, Redis |
| **Identity** | Microsoft Entra ID, managed identities, RBAC |
| **Security** | Key Vault, Defender for Cloud, Azure Policy |
| **Monitoring** | Azure Monitor, Application Insights, Log Analytics |
| **Integration** | Service Bus, Event Grid, Event Hubs, Logic Apps, API Management |
| **DevOps** | Azure DevOps, GitHub Actions, Container Registry |
| **Governance** | Cost Management, resource locks, tags, Advisor |

---

## 6. Compute choice

| Service | Use when |
|---|---|
| **Virtual Machines** | Full OS control, legacy apps, custom agents |
| **App Service** | Web apps/APIs, PaaS, deployment slots |
| **Container Apps** | Containers without Kubernetes ops, revisions, scale-to-zero |
| **AKS** | Kubernetes control, complex microservices, custom networking |
| **Functions** | Event-driven, short-lived, serverless |

**Testing rule:** the more managed the service, the more you test configuration, identity, and
integration rather than the runtime.

---

## 7. Storage

| Service | Use |
|---|---|
| **Blob** | Unstructured objects: images, backups, logs |
| **Files** | SMB file shares |
| **Disk** | Block storage for VMs |
| **Queue** | Simple message queue |
| **Table** | Key-value NoSQL |

**Redundancy:**

- **LRS** — three copies in one datacenter.
- **ZRS** — across availability zones in one region.
- **GRS** — replicated to a paired region.
- **RA-GRS** — GRS plus read access to the secondary.

**Access:** SAS tokens, managed identity, private endpoints.

---

## 8. Networking

| Service | Purpose |
|---|---|
| **VNet** | Isolated network |
| **Subnet** | Segment of a VNet |
| **NSG** | Allow/deny traffic by port/protocol/source |
| **Azure Firewall** | Managed network security |
| **Load Balancer** | Layer 4 load balancing |
| **Application Gateway** | Layer 7 load balancing, WAF, TLS termination |
| **Front Door** | Global HTTP routing, CDN, WAF |
| **Private Endpoint** | Private access to a PaaS service |
| **Azure DNS** | DNS hosting |

---

## 9. Identity, security, governance

### Identity

- **Microsoft Entra ID** — identity provider.
- **RBAC** — who can do what on which resource.
- **Managed identity** — identity for a workload; no stored credentials.
- **Service principal** — identity for an app/automation.
- **Conditional Access** — context-based access policies.
- **MFA** — multi-factor authentication.

### Security

- **Key Vault** — secrets, keys, certificates.
- **Defender for Cloud** — security posture and threat protection.
- **Azure Policy** — enforce rules (allowed regions, required tags, SKUs).
- **Resource locks** — prevent accidental delete/change.
- **Microsoft Sentinel** — SIEM/SOAR.

### Governance

- **Tags** — metadata for cost, ownership, environment.
- **Budgets** — cost alerts and limits.
- **Azure Advisor** — recommendations.
- **Cost Management** — analyze and forecast spend.

**RBAC vs Policy:** RBAC controls *who can act*; Policy controls *what is allowed to exist*.

---

## 10. Pricing and SLA

- **Pricing calculator** estimates cost before deployment.
- **TCO calculator** compares on-prem vs cloud.
- **Cost Management** tracks actual spend.
- **Reserved instances / savings plans** reduce cost for predictable workloads.
- **Spot VMs** are cheap but can be evicted.

**SLA:** provider's availability commitment. A **composite SLA** multiplies the SLAs of dependent
services, so more dependencies mean lower end-to-end availability.

Example: two services at 99.9% each → `0.999 × 0.999 = 99.8001%` combined.

---

## 11. AZ-900 traps

1. **RBAC vs Azure Policy** — access control vs resource rules.
2. **NSG vs Azure Firewall** — basic network filtering vs managed firewall.
3. **LRS vs ZRS vs GRS** — datacenter vs zone vs region redundancy.
4. **Availability zone vs region pair** — local resilience vs disaster recovery.
5. **Managed identity vs service principal** — workload identity vs app registration credential.
6. **CapEx vs OpEx** — upfront ownership vs consumption billing.
7. **Scalability vs elasticity** — capacity vs automatic adjustment.
8. **Public vs private vs hybrid** — deployment model, not service model.
9. **IaaS vs PaaS vs SaaS** — who manages the stack.
10. **SLA vs composite SLA** — single service vs end-to-end dependency chain.
