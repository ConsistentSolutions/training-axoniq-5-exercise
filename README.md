# Bike Rental — AxonIQ Training

A Spring Boot application demonstrating event-sourced bike rental with AxonIQ Framework 5.

## Prerequisites

- Java 21
- Docker

## Running

### With credentials

**1. Create credentials**

Register or log in at https://platform.axoniq.io and generate an authentication token.

**2. Start AxonServer**

Set `AXONIQ_PLATFORM_AUTHENTICATION` in a `.env` file before starting:

```
AXONIQ_PLATFORM_AUTHENTICATION=<your-token>
```

```bash
docker compose up -d
```

AxonServer dashboard is available at http://localhost:8024.

**3. Start the application**

```bash
./gradlew bootRun
```

---

### Without credentials (no token required)

> AxonServer runs without a license for up to 12 hours as a trial.
> The Axon Framework application without a license shuts down after 15 minutes.
> This is intended for evaluation purposes only.

**1. Start AxonServer**

Remove the `environment` block from `docker-compose.yml` (or leave `AXONIQ_PLATFORM_AUTHENTICATION` unset), then:

```bash
docker compose up -d
```

**2. Remove the platform dependency**

In `build.gradle.kts`, remove:

```kotlin
implementation("io.axoniq.platform:axoniq-platform-spring-boot-starter:5.1.0")
```

**3. Start the application**

```bash
./gradlew bootRun
```

## Update Checker

Axon Framework 5 includes a built-in update checker that reports available upgrades and known vulnerabilities via the application logs. It collects anonymous technical data (OS, Java version, Axon modules in use) — no personal or application-specific data. See the [AxonIQ documentation](https://docs.axoniq.io/axon-framework-update-checker/) for details.

To opt out, uncomment the following line in `src/main/resources/application.properties`:

```properties
axon.update-check.disabled=true
```

## Tests

```bash
./gradlew test
```

Tests run without AxonServer (embedded mode via `axon.axonserver.enabled=false`).
