package com.kamis.enterprise_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Size(max = 64)
    private String username;

    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    @Size(max = 128)
    private String displayName;

    @Email
    @Size(max = 128)
    private String email;

    private Long deptId; // 可空
}
