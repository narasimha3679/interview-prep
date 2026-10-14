@common @ignore
Feature: Reusable create-user helper
  Called from other features with: call read('classpath:features/common/createUser.feature') { name: '..', email: '..' }
  Enterprise DRY pattern — one place to own the create contract.

  Scenario: Create a user on JSONPlaceholder
    * url jsonPlaceholderUrl
    * path 'users'
    * request { name: '#(name)', username: '#(username)', email: '#(email)' }
    * method post
    * status 201
    * match response.id == '#number'
    * def createdUserId = response.id
    * def createdUser = response
