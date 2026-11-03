# Mobile Testing — Study Pack

Native, hybrid, and mobile-web testing: what to automate, what to test manually, and how to build a
mobile test strategy.

**Last verified:** November 2026.

---

## 60-second cram

1. **Three shapes:** native apps, hybrid apps (WebView), and mobile web. The test strategy and
   tooling differ for each.
2. **Appium is the cross-platform standard** for native/hybrid: WebDriver protocol, UiAutomator2 on
   Android, XCUITest on iOS.
3. **Mobile web is not desktop web.** Test viewport, touch, orientation, network conditions, and
   browser engines. Playwright device emulation is a good first pass, not a device farm.
4. **The device matrix is the environment.** OS version, screen size, manufacturer skin, and
   network condition change behavior.
5. **Test the mobile-specific risks:** install/upgrade, permissions, deep links, push notifications,
   offline, background/foreground, biometrics, interruptions, and battery/network.
6. **Device farms** (BrowserStack, Sauce Labs, Firebase Test Lab) provide real devices; local
   emulators are for speed, not release confidence.
7. **Flakiness is worse on mobile** because of animations, network, device state, and OS dialogs.
   Use stable selectors, explicit waits, and fresh app state.
8. **Do not automate everything.** Exploratory testing on real devices finds usability and
   device-specific bugs automation misses.

**The one line that shows depth:**

> "Mobile testing is less about the screen and more about the device lifecycle: install, permissions,
> network, backgrounding, deep links, push, and OS version. Those are the risks I design for."

---

## File map

| File | What it gives you |
|---|---|
| `01_mobile_landscape.md` | Native vs hybrid vs mobile web, risks, strategy |
| `02_appium.md` | Appium architecture, locators, waits, gestures, capabilities |
| `03_mobile_web_playwright.md` | Device emulation, touch, viewport, network |
| `04_device_farms.md` | Real devices, emulators, matrix, CI |
| `05_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Native app** | Built with platform SDKs | Appium/XCUITest/UiAutomator2 |
| **Hybrid app** | Native shell + WebView | Switch contexts; test both |
| **Mobile web** | Browser-based | Playwright/Selenium with device emulation |
| **WebView** | Embedded browser | Context switching, JS bridge |
| **Deep link** | URL/URI into app screen | Cold/warm start, auth, fallback |
| **Push notification** | OS-delivered message | Permission, delivery, tap action |
| **Device farm** | Real device cloud | Release confidence, parallel devices |
| **Emulator/simulator** | Local virtual device | Fast feedback, not full fidelity |
| **Capability** | Appium session config | Device/app/OS selection |
| **Context** | Native vs WebView | `getContextHandles`/`switchTo` |

---

## Traps

1. **Testing only on one device/OS.** Mobile bugs are device-specific.
2. **Using emulators for release confidence.** Real devices catch manufacturer/OS quirks.
3. **Ignoring permissions dialogs.** They block automation and vary by OS version.
4. **Fixed sleeps for animations.** Use waits/actionability.
5. **Shared app state between tests.** Reset app data/session.
6. **Testing only the happy path.** Offline, interruptions, backgrounding, and upgrade matter.
7. **Ignoring push/deep links.** They are common entry points and often broken.
8. **Not testing accessibility.** Screen readers, font scaling, contrast.
9. **Forgetting network conditions.** Slow/offline behavior is a mobile reality.
10. **Treating mobile web as desktop web.** Touch/viewport/engine differences matter.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, map the risks for an app you know.
- **30 minutes:** `02`, write one Appium test from memory.
- **20 minutes:** `03`, run a Playwright device emulation.
- **20 minutes:** `04`, design a device matrix.
- **30 minutes:** `05`, answer out loud.
