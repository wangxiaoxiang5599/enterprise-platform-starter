package com.kamis.enterprise_platform.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @Size(max = 128)
    private String name;

    @Size(max = 16)
    private String dataScope;
}
