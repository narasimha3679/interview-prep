# 04 — Device Farms and CI

## 1. Emulator/simulator vs real device

| Aspect | Emulator/simulator | Real device |
|---|---|---|
| Speed | Fast | Slower |
| Cost | Low | Higher |
| Fidelity | Good for logic/UI | Full OS/manufacturer/network |
| Hardware APIs | Limited | Full |
| Performance | Not representative | Real |
| Debugging | Easy | Harder |
| Use | PR smoke, fast feedback | Release confidence |

**Rule:** emulators for the inner loop; real devices for the release gate.

---

## 2. Device farms

| Provider | Strengths |
|---|---|
| **BrowserStack** | Large real-device matrix, Appium/Playwright/Selenium |
| **Sauce Labs** | Real devices, CI integrations, analytics |
| **Firebase Test Lab** | Android/iOS, Google ecosystem, Robo tests |
| **AWS Device Farm** | AWS integration, real devices |
| **Internal farm** | Control, security, custom hardware |

Choose based on device coverage, cost, CI integration, data residency, and debugging artifacts
(video, logs, network).

---

## 3. Device matrix design

Use analytics:

- Top OS versions (current + previous major).
- Top manufacturers/skins.
- Screen sizes: small, common, large, foldable, tablet.
- Low-end and high-end performance.
- Locales/regions.
- Network conditions.

Tiers:

| Tier | Devices | When |
|---|---|---|
| **Smoke** | 1 Android + 1 iOS current | PR/merge |
| **Core** | Top 5 by traffic | Nightly |
| **Full** | Long tail, old OS, foldables, tablets | Pre-release |
| **Exploratory** | New OS beta, new device | Ad hoc |

---

## 4. CI pipeline

```text
PR
  -> unit + API tests
  -> build app
  -> smoke on 1 Android + 1 iOS emulator
Merge
  -> deploy to internal track/TestFlight
  -> smoke on real devices
Nightly
  -> full regression on core matrix
Pre-release
  -> full matrix + exploratory + accessibility
```

Artifacts:

- Screenshots/video on failure.
- Device logs (logcat/console).
- Network logs.
- App build version and device metadata.
- Test report per device.

---

## 5. Parallelization

- Shard by device/OS.
- Shard by test class/tag.
- Isolate app data per test.
- Avoid sharing a single device session across tests.
- Cap concurrency to the farm's limit.
- Use unique users/data per shard.

---

## 6. Cost control

- Run smoke on emulators, not real devices.
- Run the full matrix nightly/pre-release, not every commit.
- Cache app builds.
- Use the smallest device set that covers the risk.
- Clean up app installs/sessions.
- Monitor farm usage.

---

## 7. Debugging a device-farm failure

1. Read the test report and device metadata.
2. Watch the video/screenshot.
3. Check device logs for crashes, ANRs, permission dialogs.
4. Check network logs for timeouts/errors.
5. Reproduce locally on the same OS/device if possible.
6. Determine whether it is app, test, or device/farm infrastructure.
7. Capture the artifact in the ticket.

---

## 8. Device-farm checklist

- [ ] Matrix is based on analytics.
- [ ] Smoke runs on emulators; release runs on real devices.
- [ ] App build is versioned and traceable.
- [ ] Each test gets a fresh app state.
- [ ] Screenshots/video/logs captured on failure.
- [ ] Parallel shards use unique data.
- [ ] Farm concurrency/cost is bounded.
- [ ] Results are published per device.
- [ ] Flaky device-specific tests are quarantined with an owner.
- [ ] New OS versions are tested before users upgrade.
