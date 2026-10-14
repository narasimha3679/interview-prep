# Karate Enterprise Dummy Framework

Interview-prep API automation framework built with **Maven + Java 17 + Karate 1.5.2**.

Uses public APIs (no backend to host):

| API | Purpose |
|-----|---------|
| [JSONPlaceholder](https://jsonplaceholder.typicode.com) | CRUD users/posts, schema match, data-driven tests |
| [httpbin](https://httpbin.org) | Auth pattern (login once → Bearer token reuse) |

## Prerequisites

- JDK 17+
- Maven 3.9+

## Project layout

```
src/test/java/
  karate-config.js          # env config (dev|qa|staging)
  runners/                  # JUnit 5 entry points
  features/                 # Gherkin + Karate DSL (executable specs)
  helpers/                  # Java interop utilities
  data/                     # external payloads
  schemas/                  # fuzzy schema matchers
docs/INTERVIEW_GUIDE.md     # what went where & why
```

## Run tests

```bash
# Smoke (@smoke only)
mvn test -Dtest=SmokeRunner -Dkarate.env=dev

# Full regression (everything except @ignore)
mvn test -Dtest=RegressionRunner -Dkarate.env=qa

# Maven profiles
mvn test -Psmoke
mvn test -Pregression
```

On **PowerShell**, quote `-D` args so they are not split:

```powershell
mvn test "-Dtest=SmokeRunner" "-Dkarate.env=dev"
mvn test "-Dtest=RegressionRunner" "-Dkarate.env=qa"
```

If `java` / `mvn` are not on PATH, set them once per session:

```powershell
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;C:\Users\nara\apache-maven-3.9.9\bin;$env:Path"
```

HTML reports: `target/karate-reports/`

## Tags

| Tag | Meaning |
|-----|---------|
| `@smoke` | Fast CI gate |
| `@regression` | Broader suite |
| `@users` / `@posts` / `@auth` | Domain filters |
| `@negative` | Failure-path cases |
| `@ignore` | Callable helpers — excluded from runners |

## Learn more

Read [docs/INTERVIEW_GUIDE.md](docs/INTERVIEW_GUIDE.md) for the Sr Automation Engineer walkthrough (Karate vs Cucumber, layering, interview Q&A).
