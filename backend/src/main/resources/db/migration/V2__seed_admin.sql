-- 1) tenant
insert into tenant (id, code, name, status)
values (1, 'default', 'Default Tenant', 'ACTIVE')
    on conflict (code) do nothing;

-- 2) dept
insert into dept (id, tenant_id, name, parent_id, path)
values (1, 1, 'HQ', null, '/1')
    on conflict do nothing;

-- 3) role
insert into role (id, tenant_id, code, name, data_scope)
values (1, 1, 'ADMIN', 'Administrator', 'ALL')
    on conflict (tenant_id, code) do nothing;

-- 4) permissions (示例一组，后面可扩展成全量)
insert into permission (id, code, name, type, parent_id, sort_order)
values
    (1, 'user:read',   'User Read',   'API', null, 10),
    (2, 'user:create', 'User Create', 'API', null, 20),
    (3, 'user:update', 'User Update', 'API', null, 30),
    (4, 'user:delete', 'User Delete', 'API', null, 40)
    on conflict (code) do nothing;

-- 5) role_permission
insert into role_permission (role_id, permission_id)
select 1, p.id from permission p
where p.code in ('user:read','user:create','user:update','user:delete')
    on conflict do nothing;

-- 6) admin user
-- password: Admin@1234  (BCrypt)
insert into app_user (id, tenant_id, dept_id, username, password_hash, display_name, status)
values (1, 1, 1, 'admin',
        '$2a$10$txNgNLJBPjTij7Z5ZvD5v.mEAgqG2Z9dAKMLOWUnIK9WSMam8SDdK',
        'Admin', 'ACTIVE')
    on conflict (tenant_id, username) do nothing;

-- 7) user_role
insert into user_role (user_id, role_id)
values (1, 1)
    on conflict do nothing;

select setval(pg_get_serial_sequence('tenant','id'), (select coalesce(max(id), 0) from tenant));
select setval(pg_get_serial_sequence('dept','id'), (select coalesce(max(id), 0) from dept));
select setval(pg_get_serial_sequence('role','id'), (select coalesce(max(id), 0) from role));
select setval(pg_get_serial_sequence('permission','id'), (select coalesce(max(id), 0) from permission));
select setval(pg_get_serial_sequence('app_user','id'), (select coalesce(max(id), 0) from app_user));
