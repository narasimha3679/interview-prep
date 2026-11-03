# Enterprise Selenium BDD Framework (Interview Prep)

Java 17 + Maven + Selenium 4 + Cucumber 7 + TestNG against [Sauce Demo](https://www.saucedemo.com/).

## Run

```bash
# Set JDK 17 on PATH, then:
mvn clean test
```

Optional overrides:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

## Reports

- Cucumber HTML: `target/cucumber-reports/cucumber.html`
- Extent Spark: `target/extent-reports/SparkReport.html`
- Failure screenshots: `target/screenshots/`

## Structure

See package layout under `src/test/java/com/framework/` — features in `src/test/resources/features/`.

## GitHub Actions

Two workflows live in [`.github/workflows/`](.github/workflows/):

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| [`ci.yml`](.github/workflows/ci.yml) | push to `main`, pull requests, manual | Fast feedback: runs the suite headless and uploads reports as artifacts |
| [`nightly-bdd.yml`](.github/workflows/nightly-bdd.yml) | nightly `06:00 UTC`, manual | Full nightly run that also emails a ZIP of the reports |

`ci.yml` runs headless Chrome on JDK 17 and uploads the Cucumber, Extent, and failure-screenshot artifacts (14-day retention).

### Nightly workflow (nightly + email)

Workflow: [`.github/workflows/nightly-bdd.yml`](.github/workflows/nightly-bdd.yml)

- **Schedule:** every night at `06:00 UTC`
- **Manual:** Actions → *Nightly Selenium BDD* → *Run workflow*
- On every run (pass or fail): uploads artifacts and emails a ZIP of Cucumber + Extent reports (plus screenshots if any)

### Required repository secrets

Settings → Secrets and variables → Actions:

| Secret | Example (Gmail) |
|--------|------------------|
| `SMTP_SERVER` | `smtp.gmail.com` |
| `SMTP_PORT` | `465` |
| `MAIL_USERNAME` | your Gmail address |
| `MAIL_PASSWORD` | [App Password](https://myaccount.google.com/apppasswords) (not your normal password) |
| `MAIL_FROM` | same as username (or `"Name <you@gmail.com>"`) |
| `MAIL_TO` | recipient(s), comma-separated |

For Outlook/Office365 try `smtp.office365.com` port `587` and set `secure: false` / `ignore_cert` as needed in the workflow if TLS STARTTLS is required.

---

## Related study packs

- [`../../topics/selenium/`](../../topics/selenium/) — Selenium theory
- [`../../topics/testng/`](../../topics/testng/) — TestNG lifecycle/parallel
- [`../../topics/test-strategy/`](../../topics/test-strategy/) — test levels and flakiness
- [`../../topics/ci-cd/`](../../topics/ci-cd/) — pipeline and release validation
- [`../../topics/accessibility/`](../../topics/accessibility/) — accessibility checks
