# 02 — The Framework Is a Product

This is the heart of the role. You are not the person who writes tests — you are the person who
builds **the thing many QE teams write their tests with**. That changes everything: your customers
are other engineers, and your success is measured by **their** productivity, not your test count.

> **Reframe your whole career in one line:** *"I stop being a test writer and become a platform
> engineer whose users are QE teams. My deliverable is a versioned, documented, backwards-compatible
> framework — with adoption and support, not just code."*

---

## 1. What a framework/platform team actually owns

| Owning | Not owning |
|---|---|
| The shared libraries (API client, UI driver, assertions, reporting) | Other teams' individual test suites |
| The golden-path project template + examples | Every team's product-specific test data |
| CI/CD test pipelines & the contract broker | Each service's deployment pipeline |
| Test infra (Testcontainers images, WireMock setup, env provisioning) | The services under test |
| Onboarding docs, guides, office hours | Product requirements |
| Flaky-test tooling, dashboards, quarantine | Being the bottleneck for every test |
| The upgrade/migration story across versions | Frozen, never-changing platforms |

**Say:** *"My customers are internal QE teams. I succeed when a team can stand up useful, reliable
tests on day one using our golden path — and when a breaking change in one of our dependencies
doesn't force every team to rewrite their suite."*

---

## 2. Design principles for a framework people actually adopt

1. **Golden path first.** There is one blessed way to do the common thing (write an API test, write a
   UI test). Make it the easy path. Opinionated defaults beat infinite options.
2. **Escape hatches, not walls.** Teams will hit an edge case. Give them a documented way out
   without forking the framework. (E.g., a raw `RequestSpecification` you can reach, or a way to
   bring your own reporter.)
3. **Convention over configuration.** Sensible defaults (timeouts, retries, report paths) so an empty
   test project already works.
4. **Config, not code, per environment.** URLs, credentials, timeouts injected — never hard-coded.
   (You already do this: `ConfigManager`, `karate-config.js`.)
5. **Backwards compatibility.** Teams' suites are long-lived. Breaking them is expensive and builds
   distrust. Deprecate, don't delete.
6. **Fail with actionable messages.** A framework error should say what to fix, not dump a stack
   trace from three abstraction layers deep.
7. **Documentation is a feature.** If it's not documented with a runnable example, it doesn't exist.
8. **Observability built in.** Every failure links to a report/trace/log; every run emits metrics.
9. **Boring and predictable.** The framework should be the stable ground teams build on, not a moving
   target. Resist rewrites; favor incremental evolution.
10. **Extensible by design.** Teams should compose and extend (fixtures, plugins, hooks), not fork.

**The test to apply:** *"Could a new QE engineer, who has never seen this, write a passing test in
an hour using only the README?"* If not, adoption fails.

---

## 3. Packaging & distribution (this is the "dev knowledge" they want)

Your framework must ship as a **consumable artifact** — a dependency other projects add.

### Maven/Gradle library (Java frameworks: your Karate & RestAssured projects)
- Build a **multi-module** repo:
  - `framework-core` (config, clients, assertions, reporting)
  - `framework-api` (RestAssured/Karate helpers)
  - `framework-ui` (driver, page-base, waits)
  - `framework-bom` (**Bill of Materials** — pins all versions so consumers don't fight transitive deps)
  - `framework-archetype` (generates a starter project)
- Publish to an internal repo: **Nexus / Artifactory / GitHub Packages**.
- Consumers add **one dependency** + import the **BOM**; they never manage your transitive versions.

### npm library (TS frameworks: your Playwright project)
- Publish to **npm / GitHub Packages / internal registry**.
- Version everything with **semver**; provide a `create-*` CLI or template repo.

### The BOM concept (be ready to explain)
> "A BOM is a version-management artifact. Instead of every team hand-picking compatible versions of
> RestAssured, Jackson, and Testcontainers, they import our BOM and we pin the whole set. It removes
> a whole class of 'works on my machine' dependency conflicts."

### Semantic versioning + deprecation policy
- **MAJOR** = breaking change (consumer code must change).
- **MINOR** = new feature, backwards compatible.
- **PATCH** = bug fix.
- Deprecate with warnings for at least one minor cycle, document the migration, then remove in a major.
- Publish a **CHANGELOG** and a **migration guide** per major.

**Say:** *"I treat the framework like any internal product: semver, a changelog, a deprecation
policy, and a BOM so consumers get a compatible set of versions in one line."*

---

## 4. The consumption model (how a team adopts you)

Give teams three on-ramps, in order of ease:

1. **Template / archetype** — `mvn archetype:generate` or a TS template repo produces a
   ready-to-run project with one sample test. Zero config to first green.
2. **Add the dependency** — for teams that already have a project, import the BOM + core module.
3. **Docs + examples** — a cookbook: "how to add an API test," "how to add a UI test," "how to mock a
   dependency," "how to run in CI."

**Docs that actually drive adoption:**
- `README` — 60-second quickstart.
- `GETTING_STARTED` — first test in under 10 minutes.
- `COOKBOOK` — copy-paste recipes per task.
- `MIGRATION` — per-major-version upgrade steps.
- Examples repo with real, runnable specs (your four projects are exactly this — see
  `03_reframe_your_projects.md`).

---

## 5. Release, versioning & support process

- **Trunk-based dev**, PR review, required status checks.
- **Automated releases** in CI on a version tag.
- **Release notes / changelog** updated per release.
- **Support model** — office hours, a Slack/Teams channel, an issue template, a documented SLA
  (e.g., "we respond to P1 breakages same day").
- **Feedback loop** — regular syncs with consuming teams; a visible backlog; roadmap shared with QE
  leads. Adoption dies without listening.

---

## 6. Flaky tests at scale — the thing that quietly kills frameworks

When many teams use your framework, flakiness becomes *your* problem. Have a policy ready:

1. **Detect** — track pass/fail history per test; surface flaky rate on a dashboard (not just pass/fail).
2. **Quarantine** — auto-tag flaky tests and run them in a separate, non-blocking job so they don't
   block merges, but don't disappear.
3. **Own** — every quarantined test has a named owner (the consuming team) and a deadline.
4. **Root-cause** — most flakiness is: bad waits (use auto-waiting/locators), shared state
   (isolate data), order dependence, or genuinely flaky infra. Fix the cause; retries only *hide* it.
5. **Retries** — allowed as a CI-smoke-detector, never as a fix. Report retried-but-passed separately.
6. **Metrics** — framework health = flaky rate, mean time to fix, adoption %, and run duration.

**Say:** *"At framework scale, flakiness is a product defect. I'd instrument flaky rate per test,
auto-quarantine with an owner, and fix root causes — retries just mask them. A framework people
distrust is a framework people abandon."*

---

## 7. Quality gates for the framework itself

The framework team holds *other* teams to a standard, so it must hold itself higher:

- **Unit tests for the framework** (yes, the test framework has its own tests — meta!).
- **Static analysis / linting** (Checkstyle, SpotBugs; ESLint + `tsc` for TS).
- **Code review** — at least one reviewer, checklist for backwards compatibility.
- **Dependency scanning** (Dependabot / OWASP Dependency-Check).
- **Docs lint / example builds** — docs examples are compiled and run in CI so they can't rot.
- **Coverage of public API** — the exposed surface must be tested since consumers depend on it.

---

## 8. Adoption metrics (how you prove the framework is working)

Be ready to say *how you'd know it's succeeding*:

- **Adoption:** number of teams/services onboarded, % of QE tests running on the framework.
- **Time-to-first-test:** minutes from repo creation to first green test.
- **Reliability:** flaky rate, CI pass rate.
- **Speed:** median test suite duration, parallelization.
- **Support load:** number of support requests trending down as docs improve.
- **Migration debt:** how many teams are on an old major version.

**Say:** *"I'd instrument adoption and health: teams onboarded, time-to-first-test, flaky rate, and
how many teams are stuck on an old major version. Those numbers tell me where to invest next."*

---

## 9. Anti-patterns to name (shows maturity)

- **Snowflake frameworks** — every team builds their own; the problem you're hired to solve.
- **The ivory tower** — a perfect framework no one uses because it's unopinionated and undocumented.
- **God framework** — tries to do UI, API, mobile, perf with one config; becomes unmaintainable.
- **Breaking changes without a path** — teams silently fall off the upgrade train.
- **Magic** — too much hidden behavior; when it breaks, no one can debug it.
- **Test-writing by the platform team** — the framework team must teach and enable, not become the
  bottleneck for every team's tests.
- **Retries as a strategy** — masking flakiness instead of fixing it.

---

## 10. Questions to have answers for (full answers in `04_mock_qa.md`)

- How would you design a framework that scales from 1 to 50 teams?
- How do you version and release it? How do you handle breaking changes?
- How do you get teams to adopt it? What if they resist?
- How do you support teams without becoming a bottleneck?
- How do you handle flaky tests across many teams?
- How do you decide what goes in the framework vs. stays with the product team?
- How do you measure success?
- Monorepo vs many-consumer-repos; library vs template vs platform.
- How do you keep the framework from becoming a maintenance monster?

**The through-line for all of them:** default to a **thin, opinionated golden path with escape
hatches; ship it as a versioned artifact with a BOM; document it; instrument adoption; and deprecate
rather than break.**
