package com.kamis.enterprise_platform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="tenant_id", nullable=false)
    private Long tenantId;

    @Column(nullable=false)
    private String username;

    @Column(name="password_hash", nullable=false)
    private String passwordHash;

    @Column(nullable=false)
    private String status;

    @Column(name="dept_id")
    private Long deptId;

    @Column(name="display_name")
    private String displayName;

    private String email;
}
