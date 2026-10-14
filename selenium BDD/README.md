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

## GitHub Actions (nightly + email)

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
