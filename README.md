# Bike Rental — AxonIQ Training

A Spring Boot application demonstrating an event-sourced bike rental system built with AxonIQ
Framework 5. It is the baseline for a hands-on training: a small but complete CQRS / event-sourced
slice that you extend through the exercises in [`EXERCISES.md`](EXERCISES.md).

## The domain

Members register and can be suspended. Bikes are registered at a location. A member requests a bike,
rides it, and returns it. Two read models answer different questions: which bikes are available at a
location, and what a member has rented.

## How it is structured

The code is split by bounded context and, within each, by the CQRS write/read sides:

- `bikerental/api` — the messages: `command`, `event`, and `query` records shared between sides.
- `bikerental/write/...` — command handlers and event-sourced entities (`BikeState`, `MemberState`)
  that make decisions and append events. `TripCommandHandler` shows the AF5 Dynamic Consistency
  Boundary: one handler sourcing several entities to decide.
- `bikerental/query/...` — projections that build JPA read models from the event stream and answer
  queries (`AvailableBikesProjection`, `RentalHistoryProjection`).
- `bikerental/ui` — `RentalController`, a thin REST layer over the command and query gateways.

## Training exercises

The exercises in [`EXERCISES.md`](EXERCISES.md) walk you through every part of the application — write
model, events, read model — and finish with a self-directed bonus. Exercises 1–3 come with a failing
test that is the specification; Exercise 4 and the bonus give you only the requirement. Start there.

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
