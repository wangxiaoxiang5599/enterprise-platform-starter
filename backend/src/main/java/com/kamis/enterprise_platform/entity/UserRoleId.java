package com.kamis.enterprise_platform.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleId implements Serializable {
    private Long userId;
    private Long roleId;
}
