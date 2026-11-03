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
