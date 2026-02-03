package com.kamis.enterprise_platform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "role_permission")
@IdClass(RolePermissionId.class)
@Data
public class RolePermission {
    @Id
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Id
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
