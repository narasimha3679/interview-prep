# Test Strategy for SDETs — Study Pack

How to decide what to test, where to test it, how to design cases, and how to know the suite is
trustworthy.

**Last verified:** November 2026.

---

## 60-second cram

1. **Risk decides depth.** Money, auth, data loss, and irreversible actions get more coverage than
   read-only lookups.
2. **Use the cheapest level that can catch the defect.** Unit → component → contract → API → UI →
   E2E. Push coverage down.
3. **Test levels are not a ladder.** They are a portfolio. Contracts and component tests carry most
   of the confidence in microservices.
4. **A test case is a claim about behavior.** Setup, action, expected result, and the risk it
   covers.
5. **Design techniques beat intuition.** Equivalence partitioning, boundaries, decision tables,
   state transitions, and pairwise combinations find different bugs.
6. **Test data is part of the design.** Unique per test, isolated, reproducible, and free of real
   PII.
7. **Flakiness is a product defect.** Detect, quarantine, assign, fix. Retries hide it.
8. **Metrics should change decisions.** Escaped defects, flaky rate, feedback time, and change
   failure rate are more useful than test counts.

**The one line that shows depth:**

> "My test strategy is a risk map, not a checklist. I put each risk at the cheapest level that can
> catch it, and I keep E2E for the few journeys where the integration itself is the risk."

---

## The test portfolio

```text
        /\        E2E / journey        few, critical, business-level
       /  \       --------------------------------
      /    \      UI / API acceptance  behavior through a public interface
     /      \     --------------------------------
    /        \    Contract             compatibility between services
   /          \   --------------------------------
  /            \  Component/integration  service + real infra, neighbors stubbed
 /______________\ Unit                 logic, fast, developer-owned
```

Not every system needs every layer. A monolith may have more E2E; a microservices estate needs more
contracts and component tests.

---

## File map

| File | What it gives you |
|---|---|
| `01_test_levels.md` | Unit, component, integration, contract, API, UI, E2E, regression |
| `02_test_design.md` | Risk-based design and classic test techniques |
| `03_test_data_environments.md` | Data, environments, config, secrets, isolation |
| `04_flakiness_metrics.md` | Flakiness causes, quarantine, metrics, suite health |
| `05_system_design_for_testers.md` | How to review an architecture and derive tests |
| `06_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Why it matters |
|---|---|---|
| **Risk** | Probability × impact of a failure | Decides depth and priority |
| **Oracle** | How you know the result is correct | Weak oracles produce false confidence |
| **Test level** | Unit, component, integration, contract, API, UI, E2E | Cheapest level that catches the bug |
| **Test design technique** | Method for choosing cases | Finds different classes of bugs |
| **Coverage** | What was exercised | A smell, not a target |
| **Flaky test** | Same code, different result | Destroys trust in the suite |
| **Escaped defect** | Bug found after release | Measures test effectiveness |
| **Test data** | Inputs and state | Often the real bottleneck |
| **Environment** | Where the test runs | Shared envs create contention |
| **Shift left/right** | Earlier/later quality work | Contracts + production monitoring |

---

## Traps

1. **Testing everything E2E.** Slow, flaky, hard to attribute.
2. **Chasing coverage percentage.** Executed code is not verified behavior.
3. **No oracle.** Asserting only "no exception" or "status 200."
4. **Shared test data.** Order dependence and parallel failures.
5. **Testing implementation details.** Refactors break tests without behavior changes.
6. **Only happy paths.** Most defects live in error, boundary, and partial-failure paths.
7. **Retrying flakes.** Hides the problem and erodes trust.
8. **No risk ranking.** Teams spend equal effort on low-impact paths.
9. **Environment drift.** Staging does not resemble production.
10. **No feedback loop from escaped defects.** The same gaps repeat.

---

## How to study this pack

- **10 minutes:** README + cram sheet.
- **30 minutes:** `01`, map a system you know onto the portfolio.
- **30 minutes:** `02`, design cases for one feature using three techniques.
- **20 minutes:** `03`, write a test-data and environment plan.
- **20 minutes:** `04`, define flaky-test and suite-health metrics.
- **30 minutes:** `05`, review an architecture and list the risks.
- **30 minutes:** `06`, answer out loud.
