package com.kamis.enterprise_platform.controller;

import com.kamis.enterprise_platform.dto.PermissionResponse;
import com.kamis.enterprise_platform.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping
    public List<PermissionResponse> list() {
        return permissionService.list();
    }
}
