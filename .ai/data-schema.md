# Data Schema Overview

## Core Tables
- `tenant`
  - Represents a customer organization.
  - Columns: id, code, name, status, created_at, updated_at.

- `dept`
  - Tenant-scoped departments.
  - Columns: id, tenant_id, name, parent_id, path, created_at, updated_at.

- `app_user`
  - Tenant-scoped users.
  - Columns: id, tenant_id, dept_id, username, password_hash, display_name, email, status, created_at, updated_at.

- `role`
  - Tenant-scoped roles with data scope.
  - Columns: id, tenant_id, code, name, data_scope, created_at, updated_at.

- `permission`
  - Global permissions shared across tenants.
  - Columns: id, code, name, type, parent_id, sort_order, created_at, updated_at.

- `user_role`
  - User-to-role mapping.
  - Columns: user_id, role_id, created_at.

- `role_permission`
  - Role-to-permission mapping.
  - Columns: role_id, permission_id, created_at.

- `audit_log`
  - Audit events for sensitive actions.
  - Columns: id, tenant_id, user_id, action, resource_type, resource_id,
    before_data, after_data, ip, user_agent, trace_id, created_at.

## Relations
- `tenant` 1 -> N `dept`
- `tenant` 1 -> N `app_user`
- `tenant` 1 -> N `role`
- `app_user` N -> N `role` (via `user_role`)
- `role` N -> N `permission` (via `role_permission`)
- `dept` optional parent-child hierarchy (parent_id).

## Multi-Tenancy Guidance
- Every tenant-scoped table includes `tenant_id`.
- Queries must filter on `tenant_id` to avoid data leakage.
- Permission table is global; only mapping tables are tenant-scoped through `role`.
