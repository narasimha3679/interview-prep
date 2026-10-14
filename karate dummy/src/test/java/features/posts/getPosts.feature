@posts @smoke @regression
Feature: Get posts from JSONPlaceholder
  Background:
    * url jsonPlaceholderUrl
    * callonce read('classpath:features/common/headers.feature')

  Scenario: List posts
    Given path 'posts'
    When method get
    Then status 200
    And match response == '#[100]'
    And match each response contains { userId: '#number', id: '#number', title: '#string', body: '#string' }

  Scenario: Get posts for a specific user
    Given path 'posts'
    And param userId = 1
    When method get
    Then status 200
    And match each response contains { userId: 1 }
