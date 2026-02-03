package com.kamis.enterprise_platform.service;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.dto.CreateRoleRequest;
import com.kamis.enterprise_platform.dto.RoleResponse;
import com.kamis.enterprise_platform.dto.SetRolePermissionsRequest;
import com.kamis.enterprise_platform.dto.UpdateRoleRequest;
import com.kamis.enterprise_platform.entity.Permission;
import com.kamis.enterprise_platform.entity.Role;
import com.kamis.enterprise_platform.entity.RolePermission;
import com.kamis.enterprise_platform.repository.PermissionRepository;
import com.kamis.enterprise_platform.repository.RolePermissionRepository;
import com.kamis.enterprise_platform.repository.RoleRepository;
import com.kamis.enterprise_platform.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleService(RoleRepository roleRepository,
                       PermissionRepository permissionRepository,
                       RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public List<RoleResponse> list() {
        Long tenantId = TenantContext.get();
        return roleRepository.findByTenantId(tenantId).stream()
                .map(RoleService::toResponse)
                .toList();
    }

    public RoleResponse create(CreateRoleRequest request) {
        Long tenantId = TenantContext.get();
        if (roleRepository.existsByTenantIdAndCode(tenantId, request.getCode())) {
            throw BizException.conflict("ROLE_CODE_EXISTS", "role code already exists");
        }

        Role role = new Role();
        role.setTenantId(tenantId);
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setDataScope(request.getDataScope());
        return toResponse(roleRepository.save(role));
    }

    public RoleResponse update(Long roleId, UpdateRoleRequest request) {
        Long tenantId = TenantContext.get();
        Role role = roleRepository.findByTenantIdAndId(tenantId, roleId)
                .orElseThrow(() -> BizException.notFound("ROLE_NOT_FOUND", "role not found"));

        if (request.getName() != null) {
            role.setName(request.getName());
        }
        if (request.getDataScope() != null) {
            role.setDataScope(request.getDataScope());
        }

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public void setPermissions(Long roleId, SetRolePermissionsRequest request) {
        Long tenantId = TenantContext.get();
        Role role = roleRepository.findByTenantIdAndId(tenantId, roleId)
                .orElseThrow(() -> BizException.notFound("ROLE_NOT_FOUND", "role not found"));

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
        if (permissions.size() != new HashSet<>(request.getPermissionIds()).size()) {
            throw BizException.notFound("PERMISSION_NOT_FOUND", "permission not found");
        }

        rolePermissionRepository.deleteByRoleId(role.getId());
        List<RolePermission> bindings = permissions.stream().map(permission -> {
            RolePermission rp = new RolePermission();
            rp.setRoleId(role.getId());
            rp.setPermissionId(permission.getId());
            return rp;
        }).toList();
        rolePermissionRepository.saveAll(bindings);
    }

    private static RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .dataScope(role.getDataScope())
                .build();
    }
}
