package com.framework.pages;

import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * Sauce Demo inventory / products page.
 */
public class ProductsPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");

    public boolean isLoaded() {
        return WaitUtils.urlContains("inventory") && "Products".equals(textOf(title));
    }

    public String getTitleText() {
        return textOf(title);
    }

    public ProductsPage addProductToCart(String productName) {
        // Sauce Demo button ids: "Sauce Labs Backpack" -> add-to-cart-sauce-labs-backpack
        String productSlug = productName.toLowerCase().replace(' ', '-');
        By addButton = By.id("add-to-cart-" + productSlug);
        click(addButton);
        return this;
    }

    public String getCartBadgeCount() {
        return textOf(cartBadge);
    }

    public boolean isCartBadgeVisible() {
        return isDisplayed(cartBadge);
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }
}
