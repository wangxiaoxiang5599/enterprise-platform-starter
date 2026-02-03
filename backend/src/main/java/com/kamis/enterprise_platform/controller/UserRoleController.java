package com.kamis.enterprise_platform.controller;

import com.kamis.enterprise_platform.dto.SetUserRolesRequest;
import com.kamis.enterprise_platform.service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PreAuthorize("hasAuthority('user:grant')")
    @PostMapping("/{id}/roles")
    public void setUserRoles(@PathVariable Long id, @Valid @RequestBody SetUserRolesRequest request) {
        userRoleService.setUserRoles(id, request);
    }
}
