# Constraints

## Technical Constraints
- Java 17, Spring Boot 3, Spring Security 6 are the required baseline.
- PostgreSQL is the system of record for transactional data.
- Redis is used for caching or token/session related operations.
- Flyway is the only supported database migration tool.
- JWT tokens are the primary authentication mechanism.

## Code/Design Constraints
- Do not wrap imports in try/catch blocks.
- Do not remove or rename existing public API routes without a migration plan.
- All tenant-scoped data must be filtered by tenant context.
- Avoid introducing cross-tenant queries or joins without explicit tenant filters.
- API responses must follow the ApiResponse wrapper.

## Operational Constraints
- All changes must keep the app stateless (no server-side session).
- Avoid long-running synchronous tasks in request handlers.
- Logging must not include sensitive data (passwords, secrets, tokens).
