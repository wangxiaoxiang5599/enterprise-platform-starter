-- Core tables: tenant, dept, app_user, role, permission, user_role, role_permission, audit_log

create table if not exists tenant (
                                      id            bigserial primary key,
                                      code          varchar(64) not null unique,
    name          varchar(128) not null,
    status        varchar(16) not null default 'ACTIVE',
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now()
    );

create table if not exists dept (
                                    id            bigserial primary key,
                                    tenant_id     bigint not null references tenant(id) on delete cascade,
    name          varchar(128) not null,
    parent_id     bigint null references dept(id) on delete set null,
    path          varchar(512) null,
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),
    unique(tenant_id, name, parent_id)
    );
create index if not exists idx_dept_tenant on dept(tenant_id);
create index if not exists idx_dept_parent on dept(parent_id);

create table if not exists app_user (
                                        id            bigserial primary key,
                                        tenant_id     bigint not null references tenant(id) on delete cascade,
    dept_id       bigint null references dept(id) on delete set null,
    username      varchar(64) not null,
    password_hash varchar(255) not null,
    display_name  varchar(128) null,
    email         varchar(128) null,
    status        varchar(16) not null default 'ACTIVE',
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),
    unique(tenant_id, username)
    );
create index if not exists idx_user_tenant on app_user(tenant_id);
create index if not exists idx_user_dept on app_user(dept_id);

create table if not exists role (
                                    id            bigserial primary key,
                                    tenant_id     bigint not null references tenant(id) on delete cascade,
    code          varchar(64) not null,
    name          varchar(128) not null,
    data_scope    varchar(16) not null default 'SELF',
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),
    unique(tenant_id, code)
    );
create index if not exists idx_role_tenant on role(tenant_id);

create table if not exists permission (
                                          id            bigserial primary key,
                                          code          varchar(128) not null unique,
    name          varchar(128) not null,
    type          varchar(16) not null default 'API',
    parent_id     bigint null references permission(id) on delete set null,
    sort_order    int not null default 0,
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now()
    );
create index if not exists idx_perm_parent on permission(parent_id);

create table if not exists user_role (
    user_id   bigint not null references app_user(id) on delete cascade,
    role_id   bigint not null references role(id) on delete cascade,
    created_at timestamptz not null default now(),
    primary key(user_id, role_id)
    );

create table if not exists role_permission (
    role_id       bigint not null references role(id) on delete cascade,
    permission_id bigint not null references permission(id) on delete cascade,
    created_at    timestamptz not null default now(),
    primary key(role_id, permission_id)
    );

create table if not exists audit_log (
                                         id            bigserial primary key,
                                         tenant_id     bigint not null,
                                         user_id       bigint null,
                                         action        varchar(32) not null,
    resource_type varchar(64) not null,
    resource_id   varchar(64) null,
    before_data   jsonb null,
    after_data    jsonb null,
    ip            varchar(64) null,
    user_agent    varchar(256) null,
    trace_id      varchar(64) null,
    created_at    timestamptz not null default now()
    );
create index if not exists idx_audit_tenant_time on audit_log(tenant_id, created_at desc);
create index if not exists idx_audit_user_time on audit_log(user_id, created_at desc);
create index if not exists idx_audit_action on audit_log(action);
