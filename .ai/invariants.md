# Invariants

## API Contract
- All responses must be wrapped in ApiResponse unless explicitly documented otherwise.
- Pagination responses must use PageResult for consistent metadata.
- Error responses must include a stable error code and traceId.

## Security
- New endpoints must be protected by authentication by default.
- Authorization rules must be declared using method security annotations.
- JWT parsing must validate signature and expiration.

## Multi-Tenancy
- Tenant ID must be derived from request context (X-Tenant-Id header).
- Every tenant-scoped entity must include tenant_id.
- Repository queries that access tenant data must filter by tenant_id.

## Data Integrity
- Do not modify historical Flyway migration files once applied.
- Use new migrations for schema changes.
- Passwords must always be stored as hashed values (BCrypt).

## Observability
- Every request must have a traceId propagated in logs and response headers.
- Audit logs must record actor, action, resource, and timestamp for sensitive operations.
