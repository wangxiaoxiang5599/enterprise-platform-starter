package com.kamis.enterprise_platform.service;

import com.kamis.enterprise_platform.dto.PermissionResponse;
import com.kamis.enterprise_platform.entity.Permission;
import com.kamis.enterprise_platform.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionResponse> list() {
        return permissionRepository.findAll().stream()
                .map(PermissionService::toResponse)
                .toList();
    }

    private static PermissionResponse toResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .code(permission.getCode())
                .name(permission.getName())
                .type(permission.getType())
                .parentId(permission.getParentId())
                .sortOrder(permission.getSortOrder())
                .build();
    }
}
