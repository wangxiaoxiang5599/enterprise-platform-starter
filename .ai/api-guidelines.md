# API Guidelines

## Base Conventions
- Base path: `/api`
- Use plural nouns for resource collections (e.g., `/api/users`).
- Use kebab-case for multi-word paths if needed.
- Keep endpoints RESTful unless a clear use case demands otherwise.

## Response Shape
- All responses return `ApiResponse` unless explicitly documented.
- Successful responses should include `data` and `traceId`.
- Paginated list responses must use `PageResult`.

## Error Handling
- Use consistent error codes (e.g., `USER_NOT_FOUND`).
- Do not leak internal exception messages to clients.
- Validation errors should use `VALIDATION_ERROR`.

## Authentication & Authorization
- All endpoints are authenticated by default.
- Use `@PreAuthorize` on protected methods with explicit permissions.
- Document required permissions in controller method comments.

## Validation
- Request DTOs must have validation annotations.
- Use `@Valid` in controller methods.

## Versioning
- If breaking changes are introduced, consider `/api/v2` rather than altering existing routes.
