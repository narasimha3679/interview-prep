@auth @smoke @regression
Feature: Auth pattern — login once, reuse Bearer token
  Interview pattern: login once (callonce), stash token, attach Authorization header.
  httpbin /bearer validates the Authorization header — proves the enterprise auth flow.

  Background:
    * url httpBinUrl
    * callonce read('classpath:features/common/headers.feature')
    * def auth = callonce read('classpath:features/auth/loginHelper.feature')
    * configure headers = { Authorization: '#("Bearer " + auth.token)', Accept: 'application/json' }

  Scenario: Authenticated request with reused token
    Given path 'bearer'
    When method get
    Then status 200
    And match response.authenticated == true
    And match response.token == auth.token
