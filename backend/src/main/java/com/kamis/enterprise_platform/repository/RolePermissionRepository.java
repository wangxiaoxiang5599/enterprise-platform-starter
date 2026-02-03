package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.RolePermission;
import com.kamis.enterprise_platform.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    @Modifying
    @Query("delete from RolePermission rp where rp.roleId = :roleId")
    void deleteByRoleId(Long roleId);

    @Query("select rp.permissionId from RolePermission rp where rp.roleId = :roleId")
    List<Long> findPermissionIdsByRoleId(Long roleId);
}
