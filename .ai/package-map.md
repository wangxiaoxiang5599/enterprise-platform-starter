# Package Map

## Root Namespace
- `com.kamis.enterprise_platform`

## Package Responsibilities
- `auth`:
  - Login endpoints and authentication services.
  - Token issuance and login DTOs.

- `common.api`:
  - ApiResponse wrapper, PageResult, exception handling.

- `common.trace`:
  - TraceId filter and MDC propagation.

- `config`:
  - Spring Security and application configuration.

- `controller`:
  - REST controllers and API endpoints.

- `dto`:
  - Request/response DTOs for API endpoints.

- `entity`:
  - JPA entities representing database tables.

- `repository`:
  - Spring Data repositories with tenant-aware queries.

- `security`:
  - UserDetails service, authorization setup.

- `security.jwt`:
  - JWT properties, utilities, and authentication filter.

- `service`:
  - Application services containing business logic.

- `tenant`:
  - Tenant context and tenant resolution filter.
