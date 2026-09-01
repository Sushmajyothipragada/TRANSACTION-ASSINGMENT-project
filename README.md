# Customer Transactions API

## Overview

A Spring Boot REST API for managing customer transactions. The service supports four required operations:

- Create a transaction
- Get a transaction by Transaction ID
- Update transaction status
- Get all transactions for a Customer ID

## Technologies

- Java 17
- Spring Boot / Spring Web
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5 / MockMvc
- Postman

## Transaction Fields

Each transaction contains:

- `transactionId` — Unique transaction identifier
- `customerId` — Customer identifier
- `amount` — Transaction amount
- `currency` — Supported currency
- `transactionType` — Transaction type
- `status` — Current transaction status

## Assumptions & Validation Rules

- `transactionId` and `customerId` are required.
- `transactionId` must be unique.
- `amount` must be greater than zero.
- `currency`, `transactionType`, and `status` must contain valid enum values.
- Duplicate transactions are rejected with `409 Conflict`.
- Invalid input is rejected with `400 Bad Request`.
- A transaction that does not exist returns `404 Not Found`.
- Status updates are allowed only for an existing transaction.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create transaction |
| GET | `/api/transactions/{transactionId}` | Get transaction |
| PATCH | `/api/transactions/{transactionId}/status` | Update status |
| GET | `/api/transactions/customer/{customerId}` | Get customer transactions |

## Error Handling

Custom exceptions are used for business errors:

- `DuplicateTransactionException` → `409 Conflict`
- `TransactionNotFoundException` → `404 Not Found`
- Invalid transaction data → `400 Bad Request`

`GlobalExceptionHandler` provides centralized exception handling and consistent API error responses.

## Project Structure

The application follows a layered structure:

- **Controller** — Handles REST requests
- **Service** — Contains business logic
- **Repository** — Handles database operations
- **Entity / DTO** — Represents data and request objects
- **Enums** — Defines valid transaction values
- **Exception** — Handles application-specific errors
- **Tests** — Contains automated tests

## Database

H2 in-memory database is used with Spring Data JPA.

H2 Console:

`http://localhost:8081/h2-console`

## Testing

The APIs were tested using Postman for:

- Successful transaction creation
- Retrieving transactions
- Updating transaction status
- Retrieving customer transactions
- Invalid input
- Duplicate Transaction ID
- Non-existent transactions

Automated JUnit tests cover the required scenarios, including successful creation, validation failure, duplicate ID, and transaction-not-found cases.
## Running the Application
Run tests with:

`.\mvnw.cmd clean test`
Run:
`.\mvnw.cmd spring-boot:run`
## Known Limitations
- H2 is an in-memory database, so transaction data is not retained after application restart.
- Authentication and authorization are not implemented because they were outside the scope of the challenge.

## Future Improvements

With more time, I would add persistent database support, authentication/authorization, additional automated tests, and improved API documentation.

