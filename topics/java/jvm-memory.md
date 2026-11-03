# Java — JVM Memory and Garbage Collection

## 1. Runtime memory areas

| Area | What | Shared? |
|---|---|---|
| **Heap** | Objects | Yes |
| **Stack** | Frames, locals, references | Per thread |
| **Metaspace** | Class metadata | Yes |
| **PC register** | Current instruction | Per thread |
| **Native method stack** | Native calls | Per thread |
| **Code cache** | JIT-compiled code | Yes |

---

## 2. Heap generations

```text
Young generation: Eden + Survivor spaces
Old generation: long-lived objects
```

- New objects allocate in Eden.
- Minor GC copies survivors between survivor spaces.
- Promoted objects move to old gen.
- Major/full GC collects old gen (often stop-the-world).

---

## 3. Garbage collectors

| Collector | Use |
|---|---|
| **Serial** | Small heaps, single core |
| **Parallel** | Throughput, batch |
| **G1** | Default, balanced latency/throughput |
| **ZGC** | Very low pause, large heaps |
| **Shenandoah** | Low pause, large heaps |

Tuning is workload-specific; measure before changing flags.

---

## 4. Common memory problems

### Memory leak

Objects remain reachable but unused:

- Static collections growing forever.
- Listeners/callbacks not removed.
- `ThreadLocal` not removed.
- Caches without eviction.
- Classloader leaks (redeploy).

**Symptom:** heap grows after GC; `OutOfMemoryError: Java heap space`.

### Excessive allocation

- Creating large objects in loops.
- String concatenation in loops.
- Boxing in hot paths.
- Unbounded buffering.

**Symptom:** frequent GC, high CPU.

### Metaspace leak

- Dynamic class generation/redeploy.
- `OutOfMemoryError: Metaspace`.

---

## 5. Diagnosing

```bash
jps -l                         # JVM processes
jstack <pid>                   # thread dump
jmap -heap <pid>               # heap summary
jmap -histo:live <pid>         # live object histogram
jcmd <pid> GC.heap_info
jcmd <pid> GC.class_histogram
```

Heap dump:

```bash
jcmd <pid> GC.heap_dump /tmp/heap.hprof
```

Analyze with Eclipse MAT / VisualVM / JProfiler.

---

## 6. GC logs

```bash
-Xlog:gc*:file=gc.log:time,uptime,level,tags
```

Look for:

- Pause duration/frequency.
- Heap occupancy after GC.
- Promotion rate.
- Full GC count.
- Humongous allocations (G1).

---

## 7. Testing implications

- Long-running soak tests expose leaks.
- Load tests expose allocation/GC pressure.
- Assert latency p95/p99, not just average; GC pauses create tail latency.
- Monitor heap after GC, not just used heap.
- Capture GC logs with performance results.
- Reproduce with production-like heap/data.

---

## 8. Common flags (understand, don't cargo-cult)

```bash
-Xms2g -Xmx2g          # fixed heap for predictable tests
-XX:+UseG1GC           # default on modern JDKs
-XX:MaxGCPauseMillis=200
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp
```

Fixed `-Xms`/`-Xmx` avoids heap resizing noise in performance tests.

---

## 9. JVM checklist

- [ ] Heap size explicit in performance tests.
- [ ] GC logs captured.
- [ ] Heap after GC monitored.
- [ ] Thread dumps on hang.
- [ ] Heap dump on OOM.
- [ ] `ThreadLocal` removed.
- [ ] Caches bounded.
- [ ] Listeners removed.
- [ ] Long-running soak test run.
