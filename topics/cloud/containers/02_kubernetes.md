# Kubernetes for SDETs

You do not need to administer a cluster. You need to understand how workloads are deployed and
what that means for testing.

---

## 1. Core objects

| Object | What it is | Testing implication |
|---|---|---|
| **Pod** | Smallest deployable unit, one or more containers | Ephemeral; logs disappear with the pod |
| **Deployment** | Declarative replica set + rollout strategy | Rolling updates, rollback, readiness |
| **Service** | Stable DNS + load balancing to pods | Service discovery in tests |
| **Ingress** | HTTP routing from outside the cluster | External test entry point |
| **ConfigMap** | Non-secret configuration | Config injection per environment |
| **Secret** | Secret configuration | Mounted or env-injected; never commit |
| **Namespace** | Logical isolation | Ephemeral test environments per PR |
| **Job / CronJob** | Run-to-completion workload | Test data setup, cleanup, scheduled checks |
| **StatefulSet** | Stable identity + storage | Databases, queues |
| **PersistentVolume** | Durable storage | Data survives pod restart |
| **HorizontalPodAutoscaler** | Scales replicas on metrics | Load tests must account for scale-out |
| **NetworkPolicy** | Pod-level network rules | Test allowed/denied traffic |
| **ResourceQuota / LimitRange** | Namespace resource limits | Tests can fail under constrained resources |

---

## 2. Health and rollout

### Probes

- **Liveness** — is the process alive? Failure restarts the pod.
- **Readiness** — can it serve traffic? Failure removes it from the Service.
- **Startup** — has it finished booting? Protects slow-starting apps.

**Testing rule:** a pod can be `Running` but not `Ready`. Always test readiness, not just process
state.

### Rollout

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxSurge: 1
    maxUnavailable: 0
```

`maxUnavailable: 0` keeps capacity during rollout, which makes canary/rolling validation possible.

### Rollback

```bash
kubectl rollout status deployment/listing-service -n test
kubectl rollout undo deployment/listing-service -n test
kubectl rollout history deployment/listing-service -n test
```

A rollback still needs schema compatibility. The previous version must work with the current
database.

---

## 3. Ephemeral test environments

Pattern:

```text
PR opened
  -> create namespace test-pr-1234
  -> apply manifests / Helm chart
  -> wait for readiness
  -> seed data
  -> run integration/E2E tests
  -> collect logs and artifacts
  -> delete namespace
```

Why namespaces:

- Cheap isolation compared with a full cluster per PR.
- Resource quotas prevent one PR from starving others.
- Network policies can isolate test traffic.
- Cleanup is one command.

Limitations:

- Cluster-scoped resources (CRDs, namespaces, cluster roles) are shared.
- Noisy-neighbor effects remain.
- Data stores may still be shared unless you provision per-namespace instances.

---

## 4. Testing in Kubernetes

| Layer | What to test |
|---|---|
| **Image** | Starts, correct port, non-root, read-only filesystem |
| **Config** | ConfigMap/Secret injection, missing config behavior |
| **Health** | Liveness/readiness/startup probes |
| **Service** | DNS, load balancing, session behavior |
| **Ingress** | TLS, routing, auth, rate limits |
| **Rollout** | Max unavailable/surge, readiness gating |
| **Rollback** | Previous version works with current state |
| **NetworkPolicy** | Allowed and denied traffic |
| **Resources** | CPU/memory limits, OOM behavior |
| **Autoscaling** | Scale-out latency, scale-in correctness |
| **Jobs** | Setup/cleanup idempotency |
| **Logs** | Accessible via `kubectl logs` and aggregated logging |
| **Events** | Scheduling failures, image pull errors, probe failures |

---

## 5. Debugging a failing test in a cluster

```bash
kubectl get pods -n test
kubectl describe pod <pod> -n test
kubectl logs <pod> -n test --tail=200
kubectl logs <pod> -n test --previous
kubectl get events -n test --sort-by=.lastTimestamp
kubectl exec -it <pod> -n test -- sh
kubectl port-forward svc/listing-service 8080:80 -n test
```

Common causes:

- Image pull failure (wrong tag, registry auth).
- Crash loop (bad config, missing secret, port mismatch).
- Readiness failure (dependency not ready).
- OOM kill (limit too low, memory leak).
- Network policy blocking traffic.
- Resource quota exceeded.

---

## 6. Traps

1. **Testing a `Running` pod instead of a `Ready` pod.**
2. **Assuming `kubectl apply` means the rollout finished.** Wait for `rollout status`.
3. **Sharing one namespace across PRs.** Failures become unattributable.
4. **No resource quotas.** One test run starves the cluster.
5. **No cleanup.** Namespaces and load balancers accumulate cost.
6. **Ignoring cluster-scoped resources.** CRDs and webhooks affect every namespace.
7. **Testing only through the ingress.** Internal service-to-service behavior matters too.
8. **No log aggregation.** Pod logs vanish on restart.

---

## 7. Interview line

> "I don't need to operate the cluster, but I need to know how a rollout becomes ready, how to
> isolate a test environment with a namespace, and how to read events and logs when a pod is
> running but not serving traffic. Most Kubernetes test failures are configuration, readiness, or
> resource-limit problems, not application logic."
