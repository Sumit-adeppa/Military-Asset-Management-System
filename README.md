# Military Asset Management System (MAMS)

A web application for managing military asset records and operational logistics across multiple bases. The application provides a React frontend and a Spring Boot REST API backed by PostgreSQL.

> **Scope and data warning:** This repository is a software project. Do not use it to store real, classified, or operationally sensitive military information without a formal security assessment, deployment hardening, and authorization from the relevant organization.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Application Architecture](#application-architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Create the PostgreSQL Database](#2-create-the-postgresql-database)
  - [3. Configure the Backend](#3-configure-the-backend)
  - [4. Run the Backend](#4-run-the-backend)
  - [5. Run the Frontend](#5-run-the-frontend)
- [Seed Data and Initial Accounts](#seed-data-and-initial-accounts)
- [API Overview](#api-overview)
- [Role-Based Access Control](#role-based-access-control)
- [Build and Test](#build-and-test)
- [Production Build](#production-build)
- [Configuration Reference](#configuration-reference)
- [Security Notes](#security-notes)
- [Troubleshooting](#troubleshooting)

## Features

- **Dashboard:** View asset movement totals, including opening balance, purchases, incoming and outgoing transfers, assignments, expenditures, and closing balance. The dashboard API supports optional base, equipment-type, and date filters.
- **Inventory management:** View inventory by base and equipment type and update opening balances.
- **Purchases:** Record and review equipment and supply purchases.
- **Transfers:** Initiate, approve, complete, reject, and cancel transfers between bases.
- **Assignments:** Create assignments and update their status.
- **Expenditures:** Record and review expenditure transactions.
- **Personnel management:** Maintain personnel records and their base associations.
- **Base management:** Maintain base records.
- **Equipment type management:** Maintain equipment categories and serialization types.
- **User management:** Create, view, update, and delete application users.
- **Audit logs:** Review recorded audit events.
- **Authentication:** Authenticate users with JWTs stored in an HTTP-only cookie.
- **Authorization:** Apply role-based access rules in the backend.

## Technology Stack

| Area | Technology |
|---|---|
| Backend language | Java 17 |
| Backend framework | Spring Boot 4.1.1 |
| Persistence | Spring Data JPA, Hibernate |
| API | Spring Web MVC, REST |
| Validation | Jakarta Bean Validation / Spring Boot Validation |
| Production database | PostgreSQL |
| Test database | H2 (in-memory) |
| Authentication | JSON Web Tokens (JJWT 0.12.6) |
| Password hashing | jBCrypt |
| Backend build | Maven / Maven Wrapper |
| Frontend | React 18 |
| Frontend tooling | Vite 8 |
| Routing | React Router 7 |
| HTTP client | Axios |
| Frontend quality checks | ESLint |

Dependency versions are defined in `pom.xml` and `Frontend/package.json` / `Frontend/package-lock.json`.

## Application Architecture

```text
Browser
   |
   v
React + Vite frontend (Frontend/)
   |
   | HTTP requests to /api (cookie credentials included)
   v
Spring Boot REST API (port 8080)
   |
   +-- Controllers
   +-- Services
   +-- Repositories (Spring Data JPA)
   +-- Entities / DTOs
   +-- JWT authentication filter
   +-- Role-based access interceptor
   |
   v
PostgreSQL database
```

During frontend development, the Vite development server proxies `/api` requests to `http://localhost:8080`. The backend also has a resource handler configured to serve files from `Frontend/dist/` after a frontend production build.

## Project Structure

```text
Military-Asset-Management-System/
├── .mvn/                         # Maven Wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/            # Security, web and data initialization
│   │   │   ├── controller/        # REST controllers
│   │   │   ├── dto/               # Request and response DTOs
│   │   │   ├── entity/            # JPA entities
│   │   │   ├── enums/             # Roles and status/serialization enums
│   │   │   ├── exception/         # Application exceptions
│   │   │   ├── repository/        # Spring Data repositories
│   │   │   ├── security/          # JWT and RBAC components
│   │   │   ├── service/            # Service interfaces and implementations
│   │   │   └── util/              # Utility classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/                      # Backend tests and test configuration
├── Frontend/
│   ├── public/                    # Public frontend assets
│   ├── src/
│   │   ├── components/
│   │   ├── contexts/
│   │   ├── pages/
│   │   └── services/
│   ├── package.json
│   └── vite.config.js
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Prerequisites

Install the following before running the application:

- **Java Development Kit (JDK) 17 or later**
- **PostgreSQL** and a running local PostgreSQL server
- **Node.js and npm**, using a Node.js release supported by the Vite version in `Frontend/package.json`
- Git (to clone and manage the repository)

Check the installed tools:

```bash
java -version
```

```bash
node --version
npm --version
```

```bash
psql --version
```

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Military-Asset-Management-System
```

Replace `<repository-url>` with the URL of your Git repository.

### 2. Create the PostgreSQL Database

Start PostgreSQL, connect with a PostgreSQL administrator account, and create the application database:

```sql
CREATE DATABASE military_asset_management_system;
```

The current backend configuration expects PostgreSQL at:

```text
jdbc:postgresql://localhost:5432/military_asset_management_system
```

The database username and password must match your local PostgreSQL setup.

### 3. Configure the Backend

The backend reads its primary database and JWT settings from Spring configuration. Set the following environment variables before starting the backend. Spring Boot environment variables override corresponding values in `application.properties`.

**Linux / macOS (Bash or Zsh):**

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/military_asset_management_system"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="replace-with-your-postgres-password"
export JWT_SECRET="replace-with-a-random-secret-of-at-least-32-bytes"
export DEFAULT_ADMIN_PASSWORD="replace-with-a-strong-admin-password"
export DEFAULT_COMMANDER_PASSWORD="replace-with-a-strong-commander-password"
export DEFAULT_LOGISTICS_PASSWORD="replace-with-a-strong-logistics-password"
```

Replace every example value with a private value of your own. Do not commit real passwords or JWT signing keys.

**Windows PowerShell:**

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/military_asset_management_system"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "replace-with-your-postgres-password"
$env:JWT_SECRET = "replace-with-a-random-secret-of-at-least-32-bytes"
$env:DEFAULT_ADMIN_PASSWORD = "replace-with-a-strong-admin-password"
$env:DEFAULT_COMMANDER_PASSWORD = "replace-with-a-strong-commander-password"
$env:DEFAULT_LOGISTICS_PASSWORD = "replace-with-a-strong-logistics-password"
```

For a clean setup, also replace the hard-coded database password and JWT secret currently present in `src/main/resources/application.properties` with environment-variable placeholders. For example:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/military_asset_management_system}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

The JWT signing key must be long enough for the configured HMAC algorithm. Use a securely generated random secret of at least 32 bytes; do not use the example text above as a real key.

### 4. Run the Backend

From the repository root:

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080` by default.

Check the health endpoint:

```text
GET http://localhost:8080/health
```

Expected response:

```json
{"status":"OK"}
```

### 5. Run the Frontend

Open a second terminal and run:

```bash
cd Frontend
npm ci
npm run dev
```

Open the Vite URL shown in the terminal. The configured development port is:

```text
http://localhost:5173
```

The Vite configuration proxies frontend requests beginning with `/api` to the backend at `http://localhost:8080`. Keep the backend running while using the frontend.

## Seed Data and Initial Accounts

The backend includes a `DataInitializer`. It runs by default when `app.data-initializer.enabled` is not set to `false`. When the `users` table is empty, it creates sample bases, equipment types, users, personnel, and inventory records.

The seeded usernames are:

| Username | Role |
|---|---|
| `admin` | `ADMIN` |
| `cmdrA` | `BASE_COMMANDER` |
| `cmdrB` | `BASE_COMMANDER` |
| `logistics` | `LOGISTICS_OFFICER` |

The passwords are not listed here. Set `DEFAULT_ADMIN_PASSWORD`, `DEFAULT_COMMANDER_PASSWORD`, and `DEFAULT_LOGISTICS_PASSWORD` before the first initialization. Both commander accounts use the commander password variable.

To disable automatic sample-data creation, set:

```properties
app.data-initializer.enabled=false
```

The initializer checks whether any users exist before inserting the sample data. If the database already contains users, it skips initialization.

## API Overview

The REST API is prefixed with `/api`, except for the health endpoint.

| Area | Base route | Implemented operations |
|---|---|---|
| Health | `/health` | Health status |
| Authentication | `/api/auth` | Login and logout |
| Dashboard | `/api/dashboard` | Retrieve dashboard totals and optional filters |
| Inventory | `/api/inventory` | List, retrieve, and update opening balances |
| Purchases | `/api/purchases` | Create and retrieve purchase records |
| Transfers | `/api/transfers` | Create, retrieve, approve, complete, reject, and cancel transfers |
| Assignments | `/api/assignments` | Create, retrieve, and update assignment status |
| Expenditures | `/api/expenditures` | Create and retrieve expenditure records |
| Personnel | `/api/personnel` | Create, list, retrieve, update, and delete personnel |
| Users | `/api/users` | Create, list, retrieve, update, and delete users |
| Bases | `/api/bases` | Create, list, retrieve, update, and delete bases |
| Equipment types | `/api/equipment-types` | Create, list, retrieve, update, and delete equipment types |
| Audit logs | `/api/audit-logs` | Retrieve audit records |

Authentication endpoints:

```text
POST /api/auth/login
POST /api/auth/logout
```

The login endpoint accepts a JSON request containing `username` and `password`. On successful authentication, the backend returns user information and sets an `auth_token` HTTP-only cookie. The frontend Axios client sends requests with credentials enabled.

## Role-Based Access Control

The backend defines three roles:

| Role | Access summary |
|---|---|
| `ADMIN` | Administrative and operational API access |
| `BASE_COMMANDER` | Operational access with restrictions on administrative resources and base-related access checks |
| `LOGISTICS_OFFICER` | Logistics-focused access; assignments and expenditures are restricted |

The backend enforces access through its JWT authentication filter and RBAC interceptor. Frontend navigation visibility is a usability feature; it is not a substitute for backend authorization.

## Build and Test

Run backend tests from the repository root:

```bash
./mvnw test
```

The test configuration uses an in-memory H2 database, so the test suite does not need the local PostgreSQL database.

Build the backend package:

```bash
./mvnw clean package
```

Run frontend lint checks:

```bash
cd Frontend
npm run lint
```

Create a frontend production build:

```bash
npm run build
```

Preview the built frontend locally:

```bash
npm run preview
```

## Production Build

The backend resource configuration includes `Frontend/dist/` as a static resource location. To create the frontend build and run the backend from the repository root:

```bash
cd Frontend
npm ci
npm run build
cd ..
./mvnw spring-boot:run
```

Open `http://localhost:8080` after the backend starts. The relative static-resource path is configured against the project working directory, so start the backend from the repository root.

This describes the repository's current static-file configuration, not a complete production deployment procedure. Production deployment also requires HTTPS, secure cookie settings, secret management, database backups, monitoring, and a security review.

## Configuration Reference

| Variable / Property | Purpose | Current default / behavior |
|---|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/military_asset_management_system` in the checked-in properties file |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username | `postgres` in the checked-in properties file |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password | Must be supplied privately |
| `JWT_SECRET` | JWT signing key | Must be supplied privately |
| `JWT_EXPIRATION` | JWT lifetime in milliseconds | `86400000` (24 hours) |
| `DEFAULT_ADMIN_PASSWORD` | Seeded admin password | Source currently has a fallback; override it before first initialization |
| `DEFAULT_COMMANDER_PASSWORD` | Seeded commander password | Source currently has a fallback; override it before first initialization |
| `DEFAULT_LOGISTICS_PASSWORD` | Seeded logistics password | Source currently has a fallback; override it before first initialization |
| `app.data-initializer.enabled` | Enable sample data initialization | `true` when omitted |
| `server.port` | Backend HTTP port | `8080` |
| `cors.allowed-origins` | Allowed browser origins | `http://localhost:5173` in the checked-in properties file |

`spring.jpa.hibernate.ddl-auto` is currently set to `update`. This is convenient during development, but a production environment should use a deliberate database migration and schema-management process.

## Security Notes

Before deploying or sharing this application:

1. **Remove hard-coded secrets.** The current `application.properties` contains database credentials and a JWT signing secret. Move them to environment variables or a secret manager, and rotate any value that has already been committed or shared.
2. **Replace seed-password fallbacks.** `DataInitializer` currently defines fallback values for seeded account passwords. Require strong values from a secure configuration source and disable sample initialization in production.
3. **Enable secure cookies over HTTPS.** The current login/logout implementation sets the cookie's `Secure` attribute to `false`. Review and change this for HTTPS deployments, and assess the required `SameSite` and CSRF protections for the deployment topology.
4. **Restrict CORS.** Set `cors.allowed-origins` to the exact trusted frontend origin(s). Do not use broad origins with credentialed requests.
5. **Use production database practices.** Configure least-privilege database access, backups, migrations, and appropriate monitoring.
6. **Review authorization and audit requirements.** Validate every role's access to each API operation and review whether audit coverage meets the application's requirements.

## Troubleshooting

**Backend cannot connect to PostgreSQL**

- Confirm that the PostgreSQL service is running.
- Confirm the database `military_asset_management_system` exists.
- Verify `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`.

**Frontend API requests fail**

- Confirm the backend is listening on port `8080`.
- Confirm the frontend is running on port `5173`.
- Check that the Vite proxy in `Frontend/vite.config.js` still targets `http://localhost:8080`.

**JWT startup or login errors**

- Confirm `JWT_SECRET` is set and is at least 32 bytes for the configured HMAC signing key.
- Confirm the frontend and backend are using the expected host and cookie settings.

**Frontend dependencies fail to install**

- Check that the installed Node.js version is supported by the Vite version in `Frontend/package.json`.
- Run `npm ci` from the `Frontend/` directory and retain `package-lock.json` in version control.

---

## Development Notes

- Keep `pom.xml`, Maven Wrapper scripts/configuration, `Frontend/package.json`, and `Frontend/package-lock.json` under version control.
- Do not commit `target/`, `node_modules/`, `dist/`, local environment files, credentials, database backups, or generated logs.
- Keep non-secret configuration and documented example templates in the repository; store real credentials outside source control.
