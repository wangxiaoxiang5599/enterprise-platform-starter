# System Context

## Purpose
Enterprise Platform Starter is an enterprise-grade backend foundation for building multi-tenant SaaS and AI-powered applications. The system provides core identity, access control, auditing, and tenant isolation so product teams can ship business features quickly.

## Business Domain
- **Tenants** represent separate customer organizations.
- **Departments** are tenant-scoped organizational units.
- **Users** belong to tenants and may belong to a department.
- **Roles** grant permissions and define data scope (ALL / DEPT / SELF).
- **Permissions** are fine-grained authorities used by APIs and UI actions.
- **Audit Logs** capture who did what, when, and how.

## Key Goals
- Secure authentication and authorization with JWT and RBAC.
- Strong tenant isolation in data access and authorization decisions.
- Consistent API responses and error handling.
- Traceable operations with request-level traceId and audit logs.
- Production-ready observability and operational readiness.

## Non-Goals (for now)
- Frontend UI implementation.
- Complex workflow orchestration.
- Highly specialized data science pipelines.

## High-Level Components
- **Auth**: login, token issuance, authentication filters.
- **Security**: RBAC, permission evaluation, method security.
- **Tenant**: request tenant resolution and context propagation.
- **User Management**: CRUD, profile, status management.
- **Audit**: event capture and storage.
- **Common**: response wrapping, exception handling, traceId propagation.

## Environments
- Local development uses Dockerized PostgreSQL and Redis.
- Production will run behind an API gateway with centralized logging.
