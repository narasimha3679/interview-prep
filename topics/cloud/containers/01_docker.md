# Docker for SDETs

## 1. Why Docker matters

Docker packages an application and its dependencies into an image that runs consistently across
machines. For testing it enables:

- **Reproducible environments** — the same database, broker, or service everywhere.
- **Testcontainers** — real dependencies started per test run, then destroyed.
- **Fast onboarding** — `docker compose up` instead of a setup document.
- **CI parity** — the same image locally and in the pipeline.

---

## 2. Core vocabulary

| Term | Meaning |
|---|---|
| **Image** | Immutable filesystem + metadata, built from a Dockerfile |
| **Container** | A running instance of an image |
| **Layer** | A cached filesystem change; ordering affects build speed |
| **Dockerfile** | Instructions to build an image |
| **Registry** | Stores and distributes images (Docker Hub, ACR, ECR, GCR) |
| **Volume** | Persistent storage managed by Docker |
| **Bind mount** | Host directory mounted into the container |
| **Network** | Isolated network for container-to-container DNS |
| **Compose** | Multi-container local orchestration |
| **Multi-stage build** | Build in one stage, copy only the artifact into the final image |

---

## 3. Dockerfile example

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Why multi-stage: the final image contains only the JRE and the jar, not Maven, source, or the
build cache.

Layer-caching rule: copy the dependency descriptor and resolve dependencies **before** copying
source. Source changes then don't invalidate the dependency layer.

---

## 4. Common commands

```bash
# Build and run
docker build -t my-service:local .
docker run --rm -p 8080:8080 my-service:local

# Logs and shell
docker logs -f <container>
docker exec -it <container> sh

# Compose
docker compose up -d
docker compose ps
docker compose logs -f
docker compose down -v

# Cleanup
docker system df
docker system prune -af
```

---

## 5. Compose example

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/app
      SPRING_DATASOURCE_USERNAME: app
      SPRING_DATASOURCE_PASSWORD: app
    depends_on:
      db:
        condition: service_healthy

  db:
    image: postgres:16
    environment:
      POSTGRES_DB: app
      POSTGRES_USER: app
      POSTGRES_PASSWORD: app
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U app"]
      interval: 5s
      timeout: 5s
      retries: 10
```

`depends_on` with `condition: service_healthy` waits for readiness, not just container start.

---

## 6. Testcontainers

Testcontainers starts real dependencies from test code.

```java
@Testcontainers
class ListingRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void savesListing() {
        // repository test against real Postgres
    }
}
```

Use Testcontainers for:

- Databases: Postgres, MySQL, SQL Server, Oracle.
- Messaging: Kafka, RabbitMQ, Pulsar.
- Search: Elasticsearch, OpenSearch.
- Cloud emulators: LocalStack, Azurite, Pub/Sub emulator.
- Browsers: Selenium/Playwright containers.

**Rule:** real infrastructure inside your boundary; stubs for services you don't own.

---

## 7. Testing implications

| Concern | What to verify |
|---|---|
| **Image correctness** | App starts, health endpoint responds, correct port |
| **Configuration** | Env vars override defaults; missing config fails fast |
| **Secrets** | Not baked into the image; injected at runtime |
| **Non-root user** | Container does not run as root |
| **Read-only filesystem** | App does not write to the image layer |
| **Resource limits** | App behaves under CPU/memory limits |
| **Graceful shutdown** | In-flight requests complete; connections close |
| **Health/readiness** | Distinguishes live from ready |
| **Logs** | Structured logs to stdout/stderr |
| **Image size** | Smaller image = faster pulls, smaller attack surface |
| **Vulnerabilities** | Base image scanned and patched |

---

## 8. Traps

1. **`latest` tag** — non-reproducible. Pin versions or digests.
2. **Secrets in layers** — `COPY .env` or `ARG` secrets are recoverable from image history.
3. **Running as root** — unnecessary privilege.
4. **No healthcheck** — orchestration cannot tell when the app is ready.
5. **Fat images** — build tools and caches in the runtime image.
6. **One process per container** — keep it simple; use Compose/K8s for multi-process.
7. **Ignoring `.dockerignore`** — copies `node_modules`, `.git`, and secrets into the build context.
8. **Writing data into the container** — lost on restart; use volumes or external storage.
