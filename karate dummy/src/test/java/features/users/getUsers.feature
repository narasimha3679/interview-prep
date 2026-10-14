@users @smoke @regression
Feature: Get users from JSONPlaceholder
  Background:
    * url jsonPlaceholderUrl
    * callonce read('classpath:features/common/headers.feature')

  Scenario: List users returns a non-empty array
    Given path 'users'
    When method get
    Then status 200
    And match response == '#[]'
    And match response[0] == read('classpath:schemas/user-schema.json')

  Scenario Outline: Get user by id
    Given path 'users', <userId>
    When method get
    Then status 200
    And match response.id == <userId>
    And match response == read('classpath:schemas/user-schema.json')

    Examples:
      | userId |
      | 1      |
      | 2      |
      | 5      |

  @negative
  Scenario: Get unknown user returns empty body (JSONPlaceholder quirk)
    Given path 'users', 99999
    When method get
    Then status 404
