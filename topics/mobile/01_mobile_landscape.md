# 01 — Mobile Landscape

## 1. Native vs hybrid vs mobile web

| Type | Built with | Automation | Strengths | Risks |
|---|---|---|---|---|
| **Native** | Swift/Kotlin, platform SDKs | Appium, XCUITest, UiAutomator2 | Performance, device APIs | Two codebases, OS fragmentation |
| **Hybrid** | Native shell + WebView | Appium + context switch | Reuse web code | WebView/native bridge, performance |
| **Mobile web** | HTML/CSS/JS | Playwright, Selenium | One codebase, instant updates | Browser/device differences, no native APIs |
| **Cross-platform** | React Native, Flutter, .NET MAUI | Appium/Detox/Maestro | One codebase | Bridge/native module bugs |

**Testing implication:** the same user journey needs different tooling and different risk coverage
depending on the shape.

---

## 2. Mobile-specific risks

### Lifecycle

- Install, first launch, upgrade, downgrade.
- Background/foreground, process death.
- Low memory, battery saver.
- Orientation change.
- Interruptions: call, SMS, notification, alarm.

### Permissions

- Camera, microphone, location, photos, notifications, contacts.
- Grant, deny, "ask every time", revoke later.
- OS version differences.

### Network

- Wi-Fi, cellular, roaming, airplane mode.
- Slow, lossy, offline, captive portal.
- Network switch mid-request.
- Retry/offline queue.

### Entry points

- Deep links / universal links.
- Push notification tap.
- Widgets, shortcuts, share sheet.
- QR codes, NFC.

### Device

- Screen size, density, notch, foldable.
- OS version and manufacturer skin.
- Font scaling, dark mode, locale.
- Accessibility services.

### Data and security

- Local storage/keychain.
- Biometrics.
- Certificate pinning.
- Session timeout.
- PII in logs/screenshots.
- Jailbreak/root detection.

---

## 3. Test strategy

### What to automate

- Critical journeys: login, search, product, cart, checkout, order status.
- Smoke on the supported device matrix.
- Deep links and push entry points.
- Regression of stable flows.
- API-level tests for business logic (faster than UI).

### What to keep manual/exploratory

- Usability and visual quality.
- Device-specific gestures and interruptions.
- Accessibility with a screen reader.
- Battery/performance perception.
- New OS beta behavior.
- Exploratory charters around offline/network.

### Test pyramid for mobile

```text
        few   E2E on real devices (critical journeys)
       -----
      some   UI automation on emulators/simulators
     ------
    many     API/component tests (business logic)
   --------
  most       unit tests (shared logic)
```

Do not push all coverage through the mobile UI.

---

## 4. Device matrix

Choose based on analytics, not guesswork:

- Top OS versions (current and previous major).
- Top manufacturers/skins.
- Screen sizes: small, common, large, foldable.
- Low-end and high-end performance.
- Locales/regions.
- Network conditions.

A practical matrix:

| Tier | Devices | Purpose |
|---|---|---|
| **Smoke** | 1 Android, 1 iOS (current OS) | Every PR/merge |
| **Core** | Top 5 devices by analytics | Nightly |
| **Full** | Long tail, old OS, foldables, tablets | Pre-release |

---

## 5. Appium vs Playwright vs native frameworks

| Tool | Best for | Notes |
|---|---|---|
| **Appium** | Native/hybrid cross-platform | WebDriver protocol, large ecosystem |
| **XCUITest** | iOS native | Apple-native, fast, iOS-only |
| **UiAutomator2** | Android native | Google-native, Android-only |
| **Espresso** | Android native | Fast, developer-oriented |
| **Detox** | React Native | Gray-box, fast, JS |
| **Maestro** | Simple flows | YAML, low-code, good for smoke |
| **Playwright** | Mobile web | Device emulation, not native apps |
| **Selenium** | Mobile web | Grid/device farm support |

**Interview line:** "I choose based on the app shape and the team's skills. Appium is the default
for cross-platform native/hybrid; Playwright is better for mobile web; native frameworks are faster
when you only need one platform and the team owns the app."

---

## 6. Mobile CI/CD

```text
PR
  -> unit + API tests
  -> build app (unsigned/signed)
  -> smoke on 1 Android + 1 iOS emulator/simulator
Merge
  -> deploy to internal track/TestFlight
  -> smoke on real devices
Nightly
  -> full regression on core device matrix
Pre-release
  -> full matrix + exploratory + accessibility
```

Real devices are slower and cost money; use them where fidelity matters.

---

## 7. Checklist

- [ ] App shape and supported OS versions are known.
- [ ] Device matrix is based on analytics.
- [ ] Critical journeys are automated.
- [ ] Deep links and push entry points are tested.
- [ ] Permissions are tested for grant/deny/revoke.
- [ ] Offline/slow network behavior is tested.
- [ ] Background/foreground and interruption behavior is tested.
- [ ] Install/upgrade path is tested.
- [ ] Accessibility and localization are covered.
- [ ] Real devices are used for release confidence.
