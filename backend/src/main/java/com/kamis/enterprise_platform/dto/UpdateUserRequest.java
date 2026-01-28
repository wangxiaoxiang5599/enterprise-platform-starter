package com.kamis.enterprise_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(max = 128)
    private String displayName;

    @Email
    @Size(max = 128)
    private String email;

    private Long deptId;

    @Size(max = 16)
    private String status; // ACTIVE / DISABLED
}
