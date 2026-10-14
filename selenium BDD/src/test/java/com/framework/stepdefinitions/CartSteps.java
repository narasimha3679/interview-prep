package com.framework.stepdefinitions;

import com.framework.pages.CartPage;
import com.framework.pages.ProductsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    private ProductsPage productsPage = new ProductsPage();
    private CartPage cartPage;

    @When("I add {string} to the cart")
    public void iAddToTheCart(String productName) {
        productsPage.addProductToCart(productName);
    }

    @Then("the cart badge should show {string}")
    public void theCartBadgeShouldShow(String expectedCount) {
        assertThat(productsPage.isCartBadgeVisible()).isTrue();
        assertThat(productsPage.getCartBadgeCount()).isEqualTo(expectedCount);
    }

    @When("I open the shopping cart")
    public void iOpenTheShoppingCart() {
        cartPage = productsPage.openCart();
    }

    @Then("I should be on the cart page")
    public void iShouldBeOnTheCartPage() {
        assertThat(cartPage.isLoaded()).isTrue();
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        assertThat(cartPage.hasItem(productName)).isTrue();
    }
}
