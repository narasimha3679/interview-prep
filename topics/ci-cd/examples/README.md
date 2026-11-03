# CI/CD Examples

`github-actions-ci.yml` is a generic Java + Playwright workflow. Copy it to
`.github/workflows/ci.yml` in a project and adjust paths, versions, and commands.

It demonstrates:

- PR and main triggers
- least-privilege permissions
- concurrency cancellation
- Maven and npm caching
- test report upload on `always()`
- failure artifact upload
- Playwright typecheck and smoke run

The repository's own verification workflow is at [`../../.github/workflows/verify.yml`](../../../.github/workflows/verify.yml).
