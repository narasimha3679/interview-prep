@common @ignore
Feature: Shared request headers
  Callable via callonce — sets common headers once per feature/JVM as designed by the caller.
  Maps to Cucumber shared Before hooks / common step defs for headers.

  Scenario: Apply default API headers
    * configure headers = { 'Content-Type': 'application/json', 'Accept': 'application/json', 'X-Test-Suite': 'karate-enterprise-dummy' }
