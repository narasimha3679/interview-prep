package com.framework.pages;

import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sauce Demo shopping cart page.
 */
public class CartPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By cartItemNames = By.cssSelector(".inventory_item_name");

    public boolean isLoaded() {
        return WaitUtils.urlContains("cart") && "Your Cart".equals(textOf(title));
    }

    public List<String> getCartItemNames() {
        return WaitUtils.allVisible(cartItemNames).stream()
                .map(el -> el.getText())
                .collect(Collectors.toList());
    }

    public boolean hasItem(String productName) {
        return getCartItemNames().contains(productName);
    }
}
