package com.kamis.enterprise_platform.service;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.dto.SetUserRolesRequest;
import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.entity.Role;
import com.kamis.enterprise_platform.entity.UserRole;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.repository.RoleRepository;
import com.kamis.enterprise_platform.repository.UserRoleRepository;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class UserRoleService {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(AppUserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public void setUserRoles(Long userId, SetUserRolesRequest request) {
        Long tenantId = TenantContext.get();
        AppUser user = userRepository.findByTenantIdAndId(tenantId, userId)
                .orElseThrow(() -> BizException.notFound("USER_NOT_FOUND", "user not found"));

        List<Role> roles = roleRepository.findAllById(request.getRoleIds());
        if (roles.size() != new HashSet<>(request.getRoleIds()).size()) {
            throw BizException.notFound("ROLE_NOT_FOUND", "role not found");
        }
        boolean crossTenantRole = roles.stream().anyMatch(role -> !tenantId.equals(role.getTenantId()));
        if (crossTenantRole) {
            throw BizException.forbidden("ROLE_TENANT_MISMATCH", "role belongs to different tenant");
        }

        userRoleRepository.deleteByUserId(user.getId());
        List<UserRole> bindings = roles.stream().map(role -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getId());
            ur.setRoleId(role.getId());
            return ur;
        }).toList();
        userRoleRepository.saveAll(bindings);
    }
}
