# Performance Testing — Study Pack

How to design load tests, read the results, and integrate performance checks without turning CI into
a benchmark suite.

**Last verified:** November 2026.

---

## 60-second cram

1. **Performance is a requirement, not a phase.** Define latency/throughput/error targets before you
   test.
2. **Percentiles, not averages.** p95/p99 expose the user experience; averages hide the tail.
3. **Load types:** smoke (small), load (expected), stress (beyond), soak (long), spike (sudden).
4. **Little's Law:** concurrency = throughput × latency. It explains why latency rises under load.
5. **The bottleneck moves.** Fix the DB, then the thread pool, then the network, then GC. Test again.
6. **Measure the client and the server.** Client latency without server/resource metrics is a partial
   story.
7. **CI is for regression budgets, not full benchmarks.** Keep the heavy run nightly/pre-release.
8. **A load test that doesn't assert is a demo.** Assert p95, error rate, throughput, and resource
   limits.

**The one line that shows depth:**

> "I start from the SLO, model the real traffic mix, and assert percentiles and error rate under a
> defined load. The point is not to produce a graph; it is to find the first bottleneck and prove it
> with resource metrics."

---

## File map

| File | What it gives you |
|---|---|
| `01_fundamentals.md` | Metrics, load types, Little's Law, bottlenecks |
| `02_tools.md` | k6, JMeter, Gatling, and when to use each |
| `03_ci_integration.md` | Performance budgets and pipeline design |
| `04_qa.md` | Questions with model answers |
| `examples/k6-load.js` | Runnable k6 starter script |

---

## Key terms

| Term | Meaning | Why it matters |
|---|---|---|
| **Latency** | Time per request | User experience |
| **Throughput** | Requests/sec | Capacity |
| **Concurrency** | In-flight requests | Load model |
| **p95/p99** | 95th/99th percentile latency | Tail experience |
| **Error rate** | Failed requests / total | Correctness under load |
| **Saturation** | Resource pressure | Bottleneck signal |
| **Load profile** | Traffic shape over time | Realism |
| **Think time** | Pause between user actions | Prevents unrealistic hammering |
| **Ramp-up** | Gradual increase in users | Avoids startup spike |
| **Soak** | Long-duration load | Leaks, degradation |
| **Spike** | Sudden increase | Autoscaling/queue behavior |

---

## Traps

1. **Testing without a target.** A graph with no pass/fail criterion is not a test.
2. **Using averages.** The average hides the slow tail.
3. **No think time.** Simulates robots, not users.
4. **Only one endpoint.** Real traffic is a mix.
5. **Ignoring data volume.** A test DB with 100 rows hides query problems.
6. **No server metrics.** You see latency but not why.
7. **Testing from one machine.** The load generator becomes the bottleneck.
8. **Running full load tests on every PR.** Slow, noisy, expensive.
9. **Comparing runs on different environments.** Results are not comparable.
10. **No baseline.** You cannot detect a regression without one.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, design a load profile for a checkout flow.
- **30 minutes:** `02`, write a small k6 script.
- **20 minutes:** `03`, define a CI performance budget.
- **20 minutes:** `04`, answer out loud.
