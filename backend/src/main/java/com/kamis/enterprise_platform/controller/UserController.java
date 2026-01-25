package com.kamis.enterprise_platform.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/api/users/me")
    @PreAuthorize("hasAuthority('user:read')")
    public String me() {
        return "ok";
    }
}
