# CRM System

A console-based CRM (Customer Relationship Management) application built with **Core Java, JDBC, and Multithreading** — no frameworks (no Spring, no Hibernate).

## Overview

This project manages three core entities:

- **Customer** — client companies
- **Contact** — contact persons, one of which is linked to each customer as their primary contact
- **Deal** — sales opportunities/transactions tracked against a customer, with a defined stage and amount

## Tech Stack

- **Language:** Java (OpenJDK 24)
- **Database:** PostgreSQL
- **JDBC Driver:** `org.postgresql:postgresql`
- **Connection Pooling:** HikariCP
- **Build Tool:** Maven
- **Logging:** SLF4J API

## Architecture

The project follows a classic layered architecture:

```
Model  →  DAO  →  Service  →  Console UI (Main)
```

- **`model`** — plain Java objects representing `Customer`, `Contact`, `Deal`, and the `DealStage` enum
- **`dao`** — data access layer; a generic `GenericDao<T, ID>` interface with entity-specific implementations using `PreparedStatement` exclusively (no SQL injection risk)
- **`service`** — business logic layer, including manual transaction management and concurrency control
- **`exception`** — a centralized unchecked `CustomException` used across the DAO and service layers for consistent error handling
- **`util`** — the HikariCP-backed `DatabasePool` for connection management
- **`Main`** — a console menu (Scanner-driven) exposing all CRUD operations and business flows

## Key Features

### CRUD Operations
Full create/read/update/delete support for `Customer`, `Contact`, and `Deal`, including a `findByCustomerId` lookup for deals and a `findByEmail` lookup for contacts.

### Manual Transactions
`CustomerService.createCustomerWithContact(...)` demonstrates a multi-step, atomic operation: it inserts a new `Contact`, retrieves its database-generated ID via `RETURN_GENERATED_KEYS`, and then inserts the linked `Customer` — all within a single transaction (`setAutoCommit(false)`, `commit()`, `rollback()`) to prevent partial writes.

### Concurrency Control
`DealService.updateDealAmount(...)` uses a per-resource locking strategy (`ConcurrentHashMap<Long, Object>` + `synchronized`) so that concurrent updates to the *same* deal are serialized, while updates to different deals proceed independently. Tested against multiple simulated users via `ExecutorService`.

### Business Validation
`ContactService.createContact(...)` validates contact data before persistence — name format, email format (regex), phone number format (10 digits), and duplicate-email prevention — decoupled from the raw DAO layer.

### Centralized Error Handling
A single unchecked `CustomException` wraps underlying `SQLException`s with meaningful business messages, caught and reported at the console UI layer without crashing the application.

## Project Status

Core functionality is complete and manually tested:

- ✅ Full CRUD for Customer, Contact, and Deal
- ✅ Manual transaction handling
- ✅ Concurrency-safe deal updates
- ✅ Centralized exception handling
- ✅ Business-rule validation
- ✅ Interactive console menu

## Possible Improvements

- Extend `DealService` and `CustomerService` with additional business validation
- Add automated unit/integration tests
- Add pagination for `findAll()` queries on larger datasets
- Externalize database configuration (e.g., a properties file instead of hardcoded values)
