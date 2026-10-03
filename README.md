# Employee Management System (EMS)

A modular backend application built with Java 21, Spring Boot 4, Spring Modulith, PostgreSQL 17, and Redis.

---

## Tech Stack

- **Runtime & Language**: Java 21
- **Framework**: Spring Boot 4.1, Spring Modulith 2.1
- **Persistence**: Spring Data JPA / Hibernate, Flyway (schema migrations)
- **Database**: PostgreSQL 17 (Docker)
- **Cache / Store**: Redis 7 (Docker)
- **Mapping & Validation**: MapStruct 1.6, Jakarta Bean Validation
- **API Documentation**: SpringDoc OpenAPI 3 / Swagger UI
- **Testing**: Testcontainers, JUnit 5, Mockito

---

## Architecture Overview

The system uses a **modular monolith** architecture guided by Spring Modulith. Modules enforce strict domain boundaries:

```
src/main/java/com/internal/ems/
├── common/             # Cross-cutting concerns (exceptions, global config)
├── department/         # Department management domain
└── employee/           # Employee management domain
```

- **Entities vs DTOs**: Database entities are JPA-managed classes (`@Entity`); API contracts are immutable Java `record` types.
- **Transactions**: Defaulted to `@Transactional(readOnly = true)` at service class level for read optimization; write methods explicitly declare `@Transactional`.
- **Validation & Errors**: Handled globally via `@RestControllerAdvice` returning structured `ErrorResponse` payloads.

---

## Prerequisites

- **Java 21** or later
- **Docker** and **Docker Compose**
- Git

---

## Getting Started

### 1. Configure Environment Variables

Create a `.env` file in the project root:

```env
POSTGRES_DB=ems
POSTGRES_USER=ems_user
POSTGRES_PASSWORD=changeme_local_only
REDIS_PASSWORD=changeme_local_only
```

> **Note**: `.env` is ignored by Git and should never be committed.

### 2. Start Infrastructure Containers

Start PostgreSQL and Redis:

```bash
docker compose up -d
```

| Service | Container Port | Host Port | Health Check |
|---|---|---|---|
| PostgreSQL | `5432` | `5433` | `pg_isready` |
| Redis | `6379` | `6380` | `redis-cli ping` |

### 3. Build & Run Application

Using the Maven wrapper:

```bash
# Compile and run
./mvnw spring-boot:run
```

On Windows PowerShell:
```powershell
.\mvnw.cmd spring-boot:run
```

Once started, the application listens on port `8080` with context path `/api/v1`.

---

## API Endpoints

All endpoints are versioned under `/api/v1`.

### Department Endpoints

| Method | Path | Description | Status Codes |
|---|---|---|---|
| `POST` | `/departments` | Create new department | `201`, `400`, `409` |
| `GET` | `/departments` | List all departments | `200` |
| `GET` | `/departments/{id}` | Get department by ID | `200`, `404` |
| `PUT` | `/departments/{id}` | Update department name | `200`, `400`, `404`, `409` |
| `DELETE` | `/departments/{id}` | Delete department (guarded against active employees) | `204`, `404`, `409` |

### Employee Endpoints

| Method | Path | Description | Status Codes |
|---|---|---|---|
| `POST` | `/employees` | Register employee with department | `201`, `400`, `404`, `409` |
| `GET` | `/employees` | List all employees | `200` |
| `GET` | `/employees/{id}` | Get employee by ID | `200`, `404` |
| `PUT` | `/employees/{id}` | Update employee details | `200`, `400`, `404`, `409` |
| `DELETE` | `/employees/{id}` | Delete employee | `204`, `404` |

---

## Documentation & Monitoring

When the application is running locally:

- **Swagger UI**: [http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html)
- **OpenAPI JSON Spec**: [http://localhost:8080/api/v1/v3/api-docs](http://localhost:8080/api/v1/v3/api-docs)
- **Actuator Health**: [http://localhost:8080/api/v1/actuator/health](http://localhost:8080/api/v1/actuator/health)

---

## Database Migrations (Flyway)

Flyway controls the schema lifecycle. Hibernate is configured to validate (`ddl-auto=validate`), never modify:

- `V1__init_schema.sql` — Tables for `department`, `employee`, foreign keys, and indexes.
- `V2__modulith_event_publication.sql` — Spring Modulith event publication log table.

---

## What's Coming

This project is an ongoing personal engineering build, not a snapshot. Areas being actively developed:

- **JPA & Database** - entity relationships, N+1 detection, pagination, query optimization
- **Redis Caching** - cache-aside pattern, TTL strategies, cache invalidation
- **Integration Testing** - Testcontainers (Postgres + Redis), service and repository layers
- **Spring Security** - JWT authentication, role-based access, Postgres Row-Level Security
- **Async Workflows** - Kafka for cross-domain events (employee onboarding notifications, bulk imports)
- **Observability** - structured logging, Micrometer metrics, distributed tracing with OpenTelemetry
- **Microservice Decomposition** - extract modules into independent services behind Spring Cloud Gateway
