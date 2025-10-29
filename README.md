# Event Registration & Ticket Booking

- A backend system to manage events, attendees, ticket bookings, and payments.

- The project demonstrates event lookups with Redis caching, Flyway-based database version control, role-based authorization, and integration with a fake 3rd-party payment service for testing. It also emphasises clean coding practices, structured API responses and maintainable architecture.

--------------------
## Features

1. CRUD operations for events and tickets
2. Secure booking workflow with payment simulation
3. Role-based authorization
4. Redis caching for frequently read operations
5. Flyway migrations for database versioning
6. Structured API responses
7. Clean architecture & layered codebase (controller → service → repository)
8. Logging, exception handling, and safe defaults

------------------------
## Technology Stack

- **Language** :- Java
- **Framework** :- Spring Boot
- **Build Tool** :- Gradle
- **Database** :- PostgreSQL
- **Database Migration** :- Flyway
- **Caching** :- Redis
- **Authentication & Authorization** :- Spring Security(JWT)
- **Payment Simulator** :- Fake third-party service integration
- **API format** :- JSON REST endpoints

-------------------
## Database Version Control

- Using Flyway: each schema change is tracked via versioned migrations. On application startup, Flyway checks for new migration scripts and applies them automatically, ensuring consistent database state across environments.

--------------------
## Third-Party Payment Integration

- For demonstration/testing, the app integrates with a fake third-party payment service. The interface is kept abstract so that you can replace it with a real payment gateway without major rewrites. Booking a ticket triggers payment flow: on success payment is recorded, ticket is confirmed; on failure booking is rolled back.