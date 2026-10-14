@smoke @regression
Feature: Sauce Demo Login
  As a shopper
  I want to log into Sauce Demo
  So that I can browse products

  Background:
    Given I am on the Sauce Demo login page

  @smoke
  Scenario: Successful login with valid credentials
    When I login with username "standard_user" and password "secret_sauce"
    Then I should be on the products page
    And the products page title should be "Products"

  @regression
  Scenario Outline: Unsuccessful login shows an error
    When I login with username "<username>" and password "<password>"
    Then I should see a login error containing "<message>"

    Examples:
      | username        | password     | message                                                     |
      | standard_user   | wrong_pass   | Username and password do not match any user in this service |
      | locked_out_user | secret_sauce | Sorry, this user has been locked out                        |
