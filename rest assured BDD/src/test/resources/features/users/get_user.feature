Feature: User retrieval
  As an API consumer
  I want to retrieve users from the sample API
  So that I can verify GET behaviour

  @smoke @get
  Scenario: Get an existing user
    Given the API is available
    When I request user with id 1
    Then the response status should be 200
    And the response should contain field "id"
    And the user email should not be empty

  @negative @get
  Scenario: Get a missing user returns 404
    Given the API is available
    When I request user with id 999
    Then the response status should be 404
