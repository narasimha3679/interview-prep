@common @ignore
Feature: Login helper (httpbin)
  Simulates a login call, then returns a Bearer token for callonce reuse.
  Uses httpbin so the suite needs no third-party API keys (ReqRes now requires signup).

  Scenario: Login and capture token
    * url httpBinUrl
    * path 'post'
    * request { username: 'eve.holt', password: 'cityslicka' }
    * method post
    * status 200
    * match response.json.username == 'eve.holt'
    # In real systems you read response.access_token; here we mint a demo JWT-shaped token
    * def token = 'demo-jwt-' + response.json.username + '-' + response.origin
