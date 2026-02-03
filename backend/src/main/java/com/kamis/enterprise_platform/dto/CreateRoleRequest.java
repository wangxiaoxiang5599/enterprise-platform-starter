package com.kamis.enterprise_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoleRequest {
    @NotBlank
    @Size(max = 64)
    private String code;

    @NotBlank
    @Size(max = 128)
    private String name;

    @NotBlank
    @Size(max = 16)
    private String dataScope;
}
