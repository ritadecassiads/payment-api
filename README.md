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