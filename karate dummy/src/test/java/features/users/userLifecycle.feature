@users @regression
Feature: User lifecycle — create, read, update, delete
  Demonstrates an end-to-end API flow in one feature (enterprise design pattern).

  Background:
    * url jsonPlaceholderUrl
    * callonce read('classpath:features/common/headers.feature')

  Scenario: Full CRUD flow against JSONPlaceholder
    # CREATE
    * def createResult = call read('classpath:features/common/createUser.feature') { name: 'Lifecycle User', username: 'lifecycle', email: 'lifecycle@example.com' }
    * def userId = createResult.createdUserId

    # READ (JSONPlaceholder returns static data for GET by id; assert shape)
    Given path 'users', 1
    When method get
    Then status 200
    And match response.id == 1

    # UPDATE
    Given path 'users', 1
    And request { name: 'Lifecycle Updated', email: 'updated@example.com' }
    When method put
    Then status 200
    And match response.name == 'Lifecycle Updated'

    # PATCH
    Given path 'users', 1
    And request { email: 'patched@example.com' }
    When method patch
    Then status 200
    And match response.email == 'patched@example.com'

    # DELETE
    Given path 'users', 1
    When method delete
    Then status 200
