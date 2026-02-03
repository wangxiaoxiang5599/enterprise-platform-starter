package com.kamis.enterprise_platform.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SetRolePermissionsRequest {
    @NotEmpty
    private List<Long> permissionIds;
}
