# Azure Examples

Reference-only examples for the Azure for SDET material. They are not wired into CI and require an
Azure subscription to deploy.

## Bicep test environment

```bicep
param env string
param location string = resourceGroup().location

resource plan 'Microsoft.Web/serverfarms@2023-12-01' = {
  name: 'plan-${env}'
  location: location
  sku: {
    name: 'B1'
    tier: 'Basic'
  }
  properties: {
    reserved: true
  }
}

resource app 'Microsoft.Web/sites@2023-12-01' = {
  name: 'app-${env}'
  location: location
  kind: 'app,linux'
  identity: {
    type: 'SystemAssigned'
  }
  properties: {
    serverFarmId: plan.id
    siteConfig: {
      linuxFxVersion: 'JAVA|17-java17'
      appSettings: [
        {
          name: 'ENV'
          value: env
        }
      ]
    }
  }
}

output appHostName string = app.properties.defaultHostName
output principalId string = app.identity.principalId
```

Deploy:

```bash
az group create --name "rg-listings-${ENV}" --location eastus
az deployment group create \
  --resource-group "rg-listings-${ENV}" \
  --template-file main.bicep \
  --parameters env="${ENV}"
```

Delete:

```bash
az group delete --name "rg-listings-${ENV}" --yes --no-wait
```

## GitHub Actions OIDC login

```yaml
permissions:
  id-token: write
  contents: read

steps:
  - uses: azure/login@v2
    with:
      client-id: ${{ secrets.AZURE_CLIENT_ID }}
      tenant-id: ${{ secrets.AZURE_TENANT_ID }}
      subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}

  - run: az group list --output table
```

## Notes

- Grant the federated identity the minimum role on the target resource group.
- Mark secrets/connection strings as slot settings if using App Service slots.
- Tag resources with `env`, `owner`, and `expires`.
- Delete the resource group after the test run.
