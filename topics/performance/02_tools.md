# 02 — Tools

## k6

**Language:** JavaScript/TypeScript.
**Model:** scriptable, developer-friendly, CLI-first.
**Best for:** API load tests, CI integration, modern teams.

```js
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '1m', target: 50 },   // ramp up
    { duration: '3m', target: 50 },   // steady
    { duration: '1m', target: 0 },    // ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<300', 'p(99)<800'],
    http_req_failed: ['rate<0.001'],
  },
};

export default function () {
  const res = http.get('https://api.example.test/listings');
  check(res, {
    'status is 200': (r) => r.status === 200,
    'body is not empty': (r) => r.body.length > 0,
  });
  sleep(1); // think time
}
```

Run:

```bash
k6 run load.js
```

Strengths: thresholds as code, good CLI output, CI-friendly, JS ecosystem.
Limits: less GUI, complex scenarios need more scripting.

---

## JMeter

**Language:** Java, XML test plans, GUI.
**Model:** mature, protocol-rich, GUI + CLI.
**Best for:** enterprise, complex protocols, existing JMeter skills.

Key concepts:

- Thread group = virtual users.
- Sampler = request.
- Listener = results.
- Assertion = response check.
- CSV Data Set = test data.
- Timers = think time.
- Controllers = logic/loops.

Run headless in CI:

```bash
jmeter -n -t test-plan.jmx -l results.jtl -e -o report/
```

Strengths: protocols, plugins, GUI for building, large community.
Limits: XML is hard to review, GUI encourages bad practices, resource-heavy.

---

## Gatling

**Language:** Scala/Java/Kotlin DSL.
**Model:** code-first, high-performance.
**Best for:** JVM teams, expressive scenarios, CI.

```scala
class ListingSimulation extends Simulation {
  val httpProtocol = http.baseUrl("https://api.example.test")

  val scn = scenario("Browse listings")
    .exec(http("list").get("/listings").check(status.is(200)))
    .pause(1)

  setUp(
    scn.inject(rampUsers(50).during(60.seconds))
  ).protocols(httpProtocol)
   .assertions(global.responseTime.percentile3.lt(300))
}
```

Strengths: expressive DSL, good reports, JVM integration.
Limits: Scala learning curve, smaller community than JMeter.

---

## Tool comparison

| Need | k6 | JMeter | Gatling |
|---|---|---|---|
| API load test | ✅ | ✅ | ✅ |
| GUI test builder | ❌ | ✅ | ❌ |
| Code-first | ✅ | 🟡 | ✅ |
| CI thresholds | ✅ | 🟡 | ✅ |
| Protocol breadth | 🟡 | ✅ | 🟡 |
| JVM team | 🟡 | ✅ | ✅ |
| Browser-based load | 🟡 | ✅ | 🟡 |
| Learning curve | Low | Medium | Medium |

Choose based on team skills, protocol needs, and CI integration — not on benchmark charts.

---

## Load generator sizing

- Run the generator on a separate machine/container.
- Monitor its CPU/network; if it saturates, results are invalid.
- Use distributed generators for high load.
- Pin versions and record them with results.

---

## Tool checklist

- [ ] Script is version-controlled.
- [ ] Thresholds/assertions are defined.
- [ ] Think time and traffic mix are realistic.
- [ ] Test data is parameterized.
- [ ] Load generator is monitored.
- [ ] Results include percentiles and error rate.
- [ ] Run parameters are recorded.
- [ ] Results are comparable across runs.
