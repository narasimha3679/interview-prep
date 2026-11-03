# 02 — Appium

## 1. Architecture

```text
Test code (Java/JS/Python)
   │  WebDriver protocol
   ▼
Appium server
   │
   ├── UiAutomator2 driver  -> Android device/emulator
   └── XCUITest driver      -> iOS device/simulator
```

Appium is a WebDriver server for mobile. The client sends commands; a platform driver translates
them to native automation APIs.

---

## 2. Capabilities

```java
UiAutomator2Options options = new UiAutomator2Options()
        .setPlatformName("Android")
        .setPlatformVersion("14")
        .setDeviceName("Pixel 7")
        .setApp(System.getProperty("user.dir") + "/apps/app-debug.apk")
        .setAppPackage("com.example.app")
        .setAppActivity("com.example.app.MainActivity")
        .setNoReset(false)
        .setAutoGrantPermissions(true);

AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723"), options);
```

Common capabilities:

| Capability | Purpose |
|---|---|
| `platformName` | Android/iOS |
| `platformVersion` | OS version |
| `deviceName` | Device/emulator name |
| `app` | Path/URL to the app binary |
| `appPackage` / `appActivity` | Android launch target |
| `bundleId` | iOS launch target |
| `noReset` | Preserve app state |
| `fullReset` | Uninstall/reinstall |
| `autoGrantPermissions` | Auto-grant runtime permissions |
| `udid` | Specific device |

---

## 3. Locators

Priority:

1. **Accessibility ID** — stable, cross-platform, encourages accessible apps.
2. **ID** — resource-id on Android, identifier on iOS.
3. **Class/type** — broad, brittle.
4. **XPath** — powerful but slow and brittle; use as a last resort.
5. **UIAutomator/iOS predicates** — platform-specific, can be stable.

```java
driver.findElement(AppiumBy.accessibilityId("Login"));
driver.findElement(AppiumBy.id("com.example.app:id/email"));
driver.findElement(AppiumBy.xpath("//android.widget.Button[@text='Sign in']"));
```

**Rule:** prefer accessibility IDs. They are stable and improve the app.

---

## 4. Waits

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(
        AppiumBy.accessibilityId("Welcome")));
```

Do not use fixed sleeps. Mobile animations and network variability make them unreliable.

---

## 5. Gestures

```java
// Tap
driver.findElement(AppiumBy.accessibilityId("Add to cart")).click();

// Swipe/scroll
Map<String, Object> args = Map.of(
        "left", 100, "top", 500, "width", 200, "height", 800,
        "direction", "down", "percent", 0.75);
driver.executeScript("mobile: scrollGesture", args);

// Long press
new Actions(driver)
        .clickAndHold(element)
        .pause(Duration.ofSeconds(1))
        .release()
        .perform();
```

Gestures are device/OS-sensitive; test them on real devices and keep them out of the critical path
where possible.

---

## 6. Context switching (hybrid apps)

```java
Set<String> contexts = driver.getContextHandles();
driver.context("WEBVIEW_com.example.app");
// interact with web elements
driver.context("NATIVE_APP");
```

Test:

- Native → WebView → native transitions.
- Auth/session shared between contexts.
- WebView content and native chrome.
- Back button behavior.

---

## 7. Mobile-specific commands

```java
driver.terminateApp("com.example.app");
driver.activateApp("com.example.app");
driver.runAppInBackground(Duration.ofSeconds(5));
driver.openNotifications();
driver.getBatteryInfo();
driver.getNetworkConnection();
driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
```

Use these to test lifecycle, backgrounding, and network transitions.

---

## 8. Test structure

```java
class LoginTest {

    static AndroidDriver driver;

    @BeforeAll
    static void start() throws Exception {
        // create driver with capabilities
    }

    @BeforeEach
    void reset() {
        driver.terminateApp("com.example.app");
        driver.activateApp("com.example.app");
    }

    @AfterAll
    static void stop() {
        driver.quit();
    }

    @Test
    void userCanLogIn() {
        driver.findElement(AppiumBy.accessibilityId("email")).sendKeys("buyer@example.test");
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys("secret");
        driver.findElement(AppiumBy.accessibilityId("login")).click();
        // assert home screen
    }
}
```

Use fresh app state per test or a unique user to avoid shared-state flakiness.

---

## 9. Appium traps

1. **XPath everywhere** — slow and brittle. Prefer accessibility ID.
2. **Fixed sleeps** — use explicit waits.
3. **One device** — mobile bugs are device-specific.
4. **Ignoring permissions dialogs** — they block the flow.
5. **Shared app state** — reset or use unique data.
6. **Testing on emulator only** — real devices catch manufacturer/OS quirks.
7. **No app reset between tests** — order dependence.
8. **Hardcoded device names** — use capabilities/config per environment.
9. **Ignoring keyboard** — it covers elements and changes layout.
10. **Not capturing device logs/screenshots** — mobile failures are hard to debug.

---

## 10. Appium checklist

- [ ] Capabilities are externalized per device/OS.
- [ ] Locators use accessibility IDs where possible.
- [ ] Waits are explicit.
- [ ] App state is reset per test or isolated by user.
- [ ] Permissions are handled deterministically.
- [ ] Deep links/push are tested.
- [ ] Background/foreground is tested.
- [ ] Network conditions are tested.
- [ ] Screenshots/device logs are captured on failure.
- [ ] Real devices are used for release confidence.
