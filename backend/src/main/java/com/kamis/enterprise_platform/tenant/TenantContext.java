package com.kamis.enterprise_platform.tenant;

public class TenantContext {
    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();

    public static void set(Long tenantId) { TENANT.set(tenantId); }
    public static Long get() { return TENANT.get() == null ? 1L : TENANT.get(); }
    public static void clear() { TENANT.remove(); }
}
