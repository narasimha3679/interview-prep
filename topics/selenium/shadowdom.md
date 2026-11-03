# Shadow DOM in Selenium

Shadow DOM is a browser feature that lets a component keep its own encapsulated DOM subtree, separate from the main page's DOM. Web components such as `<my-datepicker>` or `<my-login-form>` often use it so their internal markup and styles do not leak outside, and the page's CSS/JavaScript does not leak in.

```html
<my-login-form>
  #shadow-root (open)
    <input id="username" />
    <button>Login</button>
</my-login-form>
```

The `<my-login-form>` element is the shadow host, and the `#shadow-root` is the boundary. Everything inside that root is part of the shadow tree.

## Why this matters in test automation

A normal Selenium lookup like `driver.findElement(By.id("username"))` will not find elements inside a shadow root because standard DOM queries stop at the shadow boundary. XPath also cannot cross the shadow boundary by default.

## Selenium 4 solution

Use the shadow root via `getShadowRoot()`:

```java
WebElement host = driver.findElement(By.cssSelector("my-login-form"));
SearchContext shadow = host.getShadowRoot();
shadow.findElement(By.cssSelector("#username")).sendKeys("yoyo");
```

### Important limitations

- `getShadowRoot()` supports CSS selectors inside the shadow root, not XPath.
- For nested shadow roots, you repeat the process at each level:
  host → shadow root → next host → next shadow root.
- This works for open shadow roots only. Closed roots (`mode: 'closed'`) are not accessible this way, so you usually need a JavaScript workaround or a test hook exposed by developers.

## Older workaround using JavaScript executor

```java
WebElement host = driver.findElement(By.cssSelector("my-login-form"));
WebElement input = (WebElement) ((JavascriptExecutor) driver)
    .executeScript("return arguments[0].shadowRoot.querySelector('#username')", host);
```

## Comparison with other tools

- Playwright can pierce open shadow DOM with normal CSS locators, which is one of its clear advantages over Selenium.
- Cypress requires `{ includeShadowDom: true }` or a `.shadow()` command when interacting with shadow content.

## Shadow DOM vs. iframe

This is a common interview follow-up:

- An iframe is a separate document with its own browsing context, so you need `switchTo().frame()`.
- A shadow DOM is still part of the same document, but it is an encapsulated subtree. You use `getShadowRoot()` and do not switch the page context.

## Quick summary

Shadow DOM is a DOM boundary. Selenium can interact with it only through the shadow root API in Selenium 4, and you must be careful with nested roots and closed shadow DOM behavior.