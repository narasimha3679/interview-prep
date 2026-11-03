# 03 — Release Validation

Release validation answers: **"Is this exact artifact safe to promote to the next environment?"**
It is not the same as regression testing, and it is not a rubber stamp after the pipeline goes
green.

---

## 1. Release candidate model

```text
build once
   │
   ▼
artifact v1.2.3 (immutable)
   │
   ├── deploy to staging
   ├── run release validation
   ├── deploy to canary (small traffic)
   ├── monitor + smoke
   └── promote 100% or rollback
```

Rules:

1. **Build once, promote the same artifact.** Rebuilding per environment changes the thing under
   test.
2. **Version everything.** Artifact, schema, migrations, feature flags, config.
3. **Record the release candidate.** Commit SHA, artifact digest, deploy time, actor, change list.
4. **Validate the environment, not just the code.** Config, secrets, network, DNS, certificates,
   and dependencies differ by environment.

---

## 2. Release validation checklist

### Pre-deploy

- [ ] Artifact passed unit, component, contract, and API suites.
- [ ] Database migrations are backward compatible and tested.
- [ ] Feature flags have a safe default and an owner.
- [ ] Config and secrets exist in the target environment.
- [ ] Dependencies and base images are scanned.
- [ ] Rollback artifact and procedure are known.
- [ ] On-call and release owner are identified.
- [ ] Monitoring and alerts are in place for the new behavior.

### Deploy

- [ ] Deploy to staging or canary first.
- [ ] Verify health/readiness endpoints.
- [ ] Run smoke tests on critical paths.
- [ ] Validate auth and permissions.
- [ ] Validate one read and one write path per critical integration.
- [ ] Check logs for errors and stack traces.
- [ ] Check latency, error rate, and saturation.
- [ ] Verify the deployed version matches the artifact digest.

### Promote

- [ ] Canary metrics are within threshold.
- [ ] No new critical errors or alerts.
- [ ] Synthetic checks pass.
- [ ] Business metrics look normal.
- [ ] Rollback decision is explicit.
- [ ] Post-deploy verification runs after full rollout.

### Post-release

- [ ] Monitor for a defined window.
- [ ] Compare error budget burn before/after.
- [ ] Record the release and any incidents.
- [ ] Remove temporary flags and canary resources.
- [ ] Feed escaped defects back into the test strategy.

---

## 3. What to validate

| Layer | Examples | Why |
|---|---|---|
| **Artifact** | Version, checksum, signature | Ensures you deployed what you tested |
| **Health** | Liveness, readiness, startup | Catches broken deploys immediately |
| **Auth** | Login, token refresh, permissions, service-to-service | Auth is the most common release breakage |
| **Critical API** | One read + one write per core service | Proves wiring, not just process liveness |
| **Data** | Migration applied, schema compatible, seed data present | Catches invisible data failures |
| **Async** | Event published, consumed, dead-letter empty | Catches silent integration failures |
| **UI** | Login, search, checkout, critical journey | Proves the user path works |
| **Observability** | Logs, traces, metrics, alerts | Proves you can detect failure |
| **Rollback** | Previous artifact can serve traffic | Proves the escape hatch works |

Do **not** run the entire regression suite against every canary. Run a small, high-signal set and
rely on monitoring for the rest.

---

## 4. Deployment strategies

### Blue-green

Two identical environments. Deploy to green, validate, switch traffic, keep blue for rollback.

**Test focus:** green health, smoke, data compatibility, traffic switch, rollback switch.

### Canary

Send a small percentage of traffic to the new version, then increase.

**Test focus:** error rate, latency, business metrics, version-specific smoke, rollback threshold.

### Rolling

Replace instances gradually.

**Test focus:** mixed-version compatibility, session handling, database compatibility, health
checks.

### Feature flags

Deploy dark, enable for a cohort.

**Test focus:** flag on/off, cohort targeting, default safety, flag cleanup.

---

## 5. Database migrations

Migrations are the most common source of "the tests passed but the release failed."

Rules:

- **Expand/contract:** add new columns/tables first, backfill, switch reads/writes, then remove old
  structures in a later release.
- **Backward compatible:** the previous app version must still work during rolling deploy.
- **Test the migration** against a production-like dataset, including rollback if supported.
- **Measure migration time** and lock impact.
- **Never rely on destructive migrations** in a canary without a backup.

Test cases:

1. Apply migration to a copy of production-like data.
2. Start old and new app versions against the migrated schema.
3. Verify reads/writes for both versions.
4. Verify rollback or forward-fix path.
5. Check indexes, constraints, and query plans for regressions.

---

## 6. Rollback validation

A rollback is a release too.

- Know the previous artifact and config.
- Verify the rollback path in staging.
- Ensure the database schema is compatible with the previous version.
- Ensure feature flags can disable the new behavior.
- Define the trigger: error rate, latency, failed smoke, business metric.
- Define the owner and communication channel.
- After rollback, verify the same smoke set.

**Say this in an interview:**

> "I don't treat rollback as an emergency improvisation. I validate the previous artifact against
> the current schema and config in staging, and I define the metric threshold that triggers a
> rollback before the deploy starts."

---

## 7. Release validation in CI

```text
stage: deploy-staging
  -> deploy artifact
  -> wait for health
  -> run smoke + critical API + auth + migration checks
  -> publish report

stage: canary
  -> shift 5% traffic
  -> run synthetic checks
  -> watch error rate/latency for N minutes
  -> approve or auto-promote

stage: production
  -> shift 100%
  -> post-deploy verification
  -> monitor
```

Gate examples:

- Smoke pass rate = 100%.
- No new P1/P2 errors in the release window.
- Error rate delta < 0.5%.
- p95 latency delta < 10%.
- Contract `can-i-deploy` = yes.
- Security scan has no new critical/high findings.

Thresholds must be owned by the service team, not invented by the test framework.

---

## 8. Common release-validation mistakes

1. **Testing the staging build, not the production artifact.**
2. **Running E2E only and skipping auth, migrations, and async.**
3. **No rollback rehearsal.**
4. **No version verification after deploy.**
5. **Canary with too little traffic or too short a window.**
6. **Ignoring business metrics because technical metrics look fine.**
7. **No owner for the release gate.**
8. **Feature flags left on forever.**
9. **Testing only the happy path because the release is "minor."**
10. **No post-deploy monitoring window.**
