package com.kamis.enterprise_platform.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
