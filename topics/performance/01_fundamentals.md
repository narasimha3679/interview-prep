# 01 — Performance Fundamentals

## 1. Define the target first

A performance requirement looks like:

```text
Checkout API:
  p95 < 300ms, p99 < 800ms at 200 req/s
  error rate < 0.1%
  CPU < 70% per instance
  no memory growth over 2 hours
```

Without a target, a load test cannot pass or fail.

---

## 2. Metrics

| Metric | What it tells you | Watch for |
|---|---|---|
| **Latency p50** | Typical experience | Hides the tail |
| **Latency p95/p99** | Tail experience | SLO target |
| **Throughput** | Capacity | Plateaus at saturation |
| **Error rate** | Correctness under load | 5xx, timeouts, dropped |
| **Concurrency** | In-flight work | Thread/connection limits |
| **CPU** | Compute pressure | 70–80% sustained |
| **Memory** | Heap/leak | Growth over time |
| **GC** | Pause time/frequency | Stop-the-world spikes |
| **DB connections** | Pool pressure | Wait/timeout |
| **Queue depth/lag** | Backlog | Consumer saturation |
| **Network** | Bandwidth/latency | Cross-region cost |

**Percentiles:** p95 = 95% of requests are faster. p99 = 99%. Always report the percentile and the
sample size.

---

## 3. Little's Law

```text
L = λ × W
concurrency = throughput × latency
```

If throughput is 100 req/s and latency is 0.2s, concurrency is 20. If latency doubles, concurrency
doubles for the same throughput. This explains why systems saturate: more in-flight work means more
threads/connections/memory.

---

## 4. Load types

| Type | Profile | Question |
|---|---|---|
| **Smoke** | 1–5 users, short | Does the script work? |
| **Load** | Expected peak, sustained | Do we meet the SLO? |
| **Stress** | Beyond expected, ramp | Where do we break? |
| **Soak** | Expected load, hours | Leaks/degradation? |
| **Spike** | Sudden burst | Autoscale/queue behavior? |
| **Capacity** | Step increases | Max sustainable load? |

Use think time and realistic traffic mix. A load test that hits one endpoint as fast as possible is a
benchmark, not a user simulation.

---

## 5. The bottleneck moves

```text
1. Load test -> latency rises
2. Check CPU: low -> not compute
3. Check DB: connections maxed -> pool/query bottleneck
4. Fix pool/index -> latency drops, CPU rises
5. Check GC/threads -> new bottleneck
6. Repeat
```

Common bottlenecks:

- DB: missing index, N+1, locks, pool exhaustion.
- App: thread pool, connection pool, synchronous I/O.
- Cache: miss rate, stampede, eviction.
- Network: bandwidth, cross-region latency, TLS.
- Queue: consumer lag, partition count.
- JVM: GC pauses, heap pressure.
- External dependency: rate limit, latency.

---

## 6. Test data and environment

- Use production-like data volume and distribution.
- Isolate the performance environment; no other tests running.
- Warm up caches/JIT before measuring.
- Use the same instance size/config as production.
- Pin the load generator; don't let it become the bottleneck.
- Record the version, config, data size, and run parameters with the results.

---

## 7. Result interpretation

**Good sign:** throughput rises with load, latency stays flat, error rate ~0.

**Saturation:** throughput plateaus, latency rises, errors appear. Find the resource at its limit.

**Unstable:** high variance between runs. Check environment noise, data, GC, and load generator.

**Memory leak:** latency/errors degrade over a long soak; heap grows after GC.

---

## 8. Performance checklist

- [ ] SLO defined (latency, throughput, error rate).
- [ ] Traffic mix and think time modeled.
- [ ] Production-like data volume.
- [ ] Isolated environment.
- [ ] Warm-up before measurement.
- [ ] Client and server metrics collected.
- [ ] Percentiles reported, not averages.
- [ ] Bottleneck identified with resource evidence.
- [ ] Baseline stored for comparison.
- [ ] Results versioned with config/data.
