@users @regression
Feature: Create users
  Background:
    * url jsonPlaceholderUrl
    * callonce read('classpath:features/common/headers.feature')
    * def DataGenerator = Java.type('helpers.DataGenerator')

  Scenario: Create user with Java-generated dynamic data
    * def payload =
      """
      {
        name: '#(DataGenerator.randomName())',
        username: 'auto_user',
        email: '#(DataGenerator.randomEmail())'
      }
      """
    Given path 'users'
    And request payload
    When method post
    Then status 201
    And match response.id == '#number'
    And match response.name == payload.name
    And match response.email == payload.email

  Scenario: Create user from external JSON data file
    * def users = read('classpath:data/users.json')
    * def first = users[0]
    Given path 'users'
    And request first
    When method post
    Then status 201
    And match response.name == first.name
    And match response.email == first.email

  Scenario: Create user via reusable common feature
    * def result = call read('classpath:features/common/createUser.feature') { name: 'Reusable Call', username: 'reusable', email: 'reusable@example.com' }
    * match result.createdUserId == '#number'
    * match result.createdUser.name == 'Reusable Call'
