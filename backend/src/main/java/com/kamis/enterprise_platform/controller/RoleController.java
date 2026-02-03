package com.kamis.enterprise_platform.controller;

import com.kamis.enterprise_platform.dto.CreateRoleRequest;
import com.kamis.enterprise_platform.dto.RoleResponse;
import com.kamis.enterprise_platform.dto.SetRolePermissionsRequest;
import com.kamis.enterprise_platform.dto.UpdateRoleRequest;
import com.kamis.enterprise_platform.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAuthority('role:read')")
    @GetMapping
    public List<RoleResponse> list() {
        return roleService.list();
    }

    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
        return roleService.create(request);
    }

    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        return roleService.update(id, request);
    }

    @PreAuthorize("hasAuthority('role:grant')")
    @PostMapping("/{id}/permissions")
    public void setPermissions(@PathVariable Long id, @Valid @RequestBody SetRolePermissionsRequest request) {
        roleService.setPermissions(id, request);
    }
}
