@regression
Feature: Sauce Demo Cart
  As a shopper
  I want to add products to my cart
  So that I can purchase them later

  Background:
    Given I am on the Sauce Demo login page
    And I login with username "standard_user" and password "secret_sauce"
    And I should be on the products page

  @smoke
  Scenario: Add a product to the cart
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"
    When I open the shopping cart
    Then I should be on the cart page
    And the cart should contain "Sauce Labs Backpack"
