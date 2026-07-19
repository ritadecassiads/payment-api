# Payment API

A simple REST API that simulates basic banking operations using in-memory storage.

This project was developed as part of a technical assessment using **Java 21** and **Spring Boot**.

## Features

The API supports the following operations:

- Reset application state
- Create accounts through deposits
- Retrieve account balances
- Deposit funds
- Withdraw funds
- Transfer funds between accounts

## Technologies

- Java 21
- Spring Boot
- Maven
- Lombok

> This project includes the Maven Wrapper (`mvnw`), so a separate Maven installation is not required.

## Project Structure

```
src
├── controller
├── service
├── repository
├── model
└── dto
```

The application follows a layered architecture:

- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains the business logic.
- **Repository**: Stores account data in memory.
- **DTOs**: Represent request and response payloads.
- **Model**: Represents the domain entity.

## Running the Application

### Prerequisites

- Java 21
- Ensure `JAVA_HOME` points to your Java 21 installation.

### Clone the repository

```bash
git clone https://github.com/ritadecassiads/payment-api.git
cd payment-api
```

### Build the project

**Linux/macOS (or Git Bash on Windows)**

```bash
./mvnw clean install
```

**Windows Command Prompt / PowerShell**

```cmd
mvnw.cmd clean install
```

### Run the application

**Linux/macOS (or Git Bash on Windows)**

```bash
./mvnw spring-boot:run
```

**Windows Command Prompt / PowerShell**

```cmd
mvnw.cmd spring-boot:run
```

The application will start at:

```
http://localhost:8080
```

## Available Endpoints

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/reset` | Reset application state |
| GET | `/balance?account_id={id}` | Retrieve account balance |
| POST | `/event` | Process deposit, withdrawal or transfer |

## Notes

- All data is stored **in memory**.
- No database is required.
- Account creation occurs automatically when a deposit is made to a non-existing account.
- Transfers update both accounts within a single service operation.

## Design decisions

This project intentionally favors simplicity and clarity for the assessment context. The main design choices are described below to help reviewers and future maintainers understand trade-offs and potential migration paths.

- Layered architecture: the codebase follows Controller -> Service -> Repository layers to separate responsibilities and improve readability, maintainability and testability.
- In-memory persistence: `AccountRepository` uses a `ConcurrentHashMap` to store accounts. This keeps the project self-contained (no DB) and easy to run for tests and demos.
- `ConcurrentHashMap`: chosen instead of `HashMap` because the application may receive concurrent requests; it provides thread-safe access for basic operations without external synchronization.
- `synchronized` on service methods: `processEvent()` and related service methods are synchronized to avoid race conditions when multiple threads modify balances concurrently in this in-memory implementation. In a production system backed by a database, prefer transactional boundaries and database concurrency control instead of coarse synchronization.
- `Long` for balances: balances and amounts use `Long` (instead of `Integer`) to support a larger numeric range. For real financial systems that require fractional values, use `BigDecimal`.
- Why not `Optional`: the repository currently returns `null` when an account is not found to keep the in-memory API simple and avoid extra wrapping in this small codebase. If migrating to Spring Data JPA, change `findById` to return `Optional<Account>` (the preferred pattern in that API) and adapt service code accordingly.
- Unit tests: business rules are covered at the service layer using JUnit 5 and Mockito to keep tests focused on logic and independent from the web layer and repository implementation.

Migration notes

- To migrate to a persistent store (Spring Data JPA):
  - Change repository signatures to use `Optional<Account>` where appropriate.
  - Replace `synchronized` protection with transactional boundaries (`@Transactional`) and rely on database concurrency controls.
  - Replace `Long` with `BigDecimal` if handling fractional currency values is required.


## Example Request

Deposit:

```http
POST /event
```

```json
{
  "type": "deposit",
  "destination": "100",
  "amount": 10
}
```

Transfer:

```json
{
  "type": "transfer",
  "origin": "100",
  "destination": "200",
  "amount": 5
}
```

Withdraw:

```json
{
  "type": "withdraw",
  "origin": "100",
  "amount": 5
}
```