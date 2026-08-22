# CRM System

A console-based CRM (Customer Relationship Management) application built with **Core Java, JDBC, and Multithreading**.

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


## Project Status

Core functionality is complete and manually tested:

-  Full CRUD for Customer, Contact, and Deal
-  Manual transaction handling
-  Concurrency-safe deal updates
-  Centralized exception handling
-  Business-rule validation
-  Interactive console menu

## Possible Improvements

- Extend `DealService` and `CustomerService` with additional business validation
- Add automated unit/integration tests
- Add pagination for `findAll()` queries on larger datasets
- Externalize database configuration (e.g., a properties file instead of hardcoded values)
