Feature: User create and update
  As an API consumer
  I want to create and update users on the sample API
  So that I can verify write behaviour

  @smoke @create
  Scenario: Create a user from a JSON payload file
    Given the API is available
    When I create a user from payload "create_user.json"
    Then the response status should be 201
    And the created user should echo name "Neo" and job "The One"

  @create
  Scenario: Create a user with inline name and job
    Given the API is available
    When I create a user named "Trinity" with job "Operator"
    Then the response status should be 201
    And the created user should echo name "Trinity" and job "Operator"

  @update
  Scenario: Update an existing user
    Given the API is available
    When I update user 1 with name "Morpheus" and job "Captain"
    Then the response status should be 200
    And the updated user should echo name "Morpheus" and job "Captain"

  @delete
  Scenario: Delete a user
    Given the API is available
    When I delete user with id 1
    Then the response status should be 200
