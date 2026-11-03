# Cloud Provider Mapping

Use this table to answer "any cloud is fine" in provider-neutral language, then anchor on Azure.

| Concept | Azure | AWS | GCP |
|---|---|---|---|
| **Virtual machines (IaaS)** | Virtual Machines | EC2 | Compute Engine |
| **Managed app platform (PaaS)** | App Service | Elastic Beanstalk / App Runner | App Engine / Cloud Run |
| **Containers (managed)** | Container Apps | ECS Fargate / App Runner | Cloud Run |
| **Kubernetes** | AKS | EKS | GKE |
| **Serverless functions** | Azure Functions | Lambda | Cloud Functions |
| **Object storage** | Blob Storage | S3 | Cloud Storage |
| **Block storage** | Managed Disks | EBS | Persistent Disk |
| **File storage** | Azure Files | EFS / FSx | Filestore |
| **Managed SQL** | Azure SQL Database | RDS | Cloud SQL |
| **Managed PostgreSQL/MySQL** | Azure Database for PostgreSQL/MySQL | RDS / Aurora | Cloud SQL / AlloyDB |
| **NoSQL** | Cosmos DB | DynamoDB | Firestore / Bigtable |
| **Cache** | Azure Cache for Redis | ElastiCache | Memorystore |
| **Message queue** | Queue Storage / Service Bus | SQS | Pub/Sub |
| **Pub/sub messaging** | Service Bus topics / Event Grid | SNS | Pub/Sub |
| **Event streaming** | Event Hubs | Kinesis | Pub/Sub / Dataflow |
| **API gateway** | API Management | API Gateway | Apigee / API Gateway |
| **Identity** | Microsoft Entra ID | IAM / Cognito | Cloud IAM / Identity Platform |
| **Secrets** | Key Vault | Secrets Manager | Secret Manager |
| **CI/CD** | Azure Pipelines | CodePipeline / CodeBuild | Cloud Build |
| **Container registry** | Azure Container Registry | ECR | Artifact Registry |
| **Monitoring** | Azure Monitor | CloudWatch | Cloud Monitoring |
| **Logging** | Log Analytics | CloudWatch Logs | Cloud Logging |
| **Tracing** | Application Insights | X-Ray | Cloud Trace |
| **IaC** | Bicep / ARM / Terraform | CloudFormation / Terraform | Deployment Manager / Terraform |
| **Policy** | Azure Policy | AWS Config / SCPs | Organization Policy |
| **Cost management** | Cost Management + Billing | Cost Explorer / Budgets | Cloud Billing |
| **CDN** | Azure Front Door / CDN | CloudFront | Cloud CDN |
| **DNS** | Azure DNS | Route 53 | Cloud DNS |
| **Load balancer** | Azure Load Balancer / App Gateway | ALB / NLB | Cloud Load Balancing |
| **Private connectivity** | Private Endpoint / Private Link | PrivateLink | Private Service Connect |
| **Local emulators** | Azurite, Cosmos DB emulator, Functions Core Tools | LocalStack, DynamoDB Local | Pub/Sub emulator, Firestore emulator, BigQuery emulator |

## Provider-neutral interview answers

**"Which cloud have you used?"**

> "Azure is my primary cloud — I have AZ-900 and I've worked with [the services you actually used].
> The concepts transfer: managed identities, resource groups, deployment slots, and managed
> messaging exist in equivalent forms on AWS and GCP. I'd ramp on the provider-specific console and
> IAM model quickly because the testing approach is the same."

**"How would you build a test environment in the cloud?"**

> "Provision it from code into an isolated subscription/resource group/namespace, give it a
> managed identity, load secrets from the secret store, seed data through APIs, run the suite,
> publish artifacts, and destroy it. The provider changes; the pattern doesn't."

**"What's different about testing in the cloud?"**

> "Identity, network boundaries, quotas, managed-service limits, region behavior, and cost become
> part of the test surface. You also lose some local reproducibility, so you need emulators for the
> fast loop and real cloud environments for release validation."
