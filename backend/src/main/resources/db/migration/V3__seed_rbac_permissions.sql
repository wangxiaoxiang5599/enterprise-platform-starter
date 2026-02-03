-- Additional RBAC permissions
insert into permission (code, name, type, parent_id, sort_order)
values
    ('role:read', 'Role Read', 'API', null, 110),
    ('role:create', 'Role Create', 'API', null, 120),
    ('role:update', 'Role Update', 'API', null, 130),
    ('role:grant', 'Role Grant', 'API', null, 140),
    ('permission:read', 'Permission Read', 'API', null, 150),
    ('user:grant', 'User Grant', 'API', null, 160)
    on conflict (code) do nothing;

insert into role_permission (role_id, permission_id)
select 1, p.id from permission p
where p.code in ('role:read','role:create','role:update','role:grant','permission:read','user:grant')
    on conflict do nothing;
