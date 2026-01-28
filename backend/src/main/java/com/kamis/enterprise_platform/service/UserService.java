package com.kamis.enterprise_platform.service;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.dto.CreateUserRequest;
import com.kamis.enterprise_platform.dto.UpdateUserRequest;
import com.kamis.enterprise_platform.dto.UserResponse;
import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Page<UserResponse> list(Pageable pageable) {
        Long tenantId = TenantContext.get();
        return repo.findByTenantId(tenantId, pageable)
                .map(UserService::toResp);
    }

    public UserResponse create(CreateUserRequest req) {
        Long tenantId = TenantContext.get();

        if (repo.existsByTenantIdAndUsername(tenantId, req.getUsername())) {
            throw BizException.conflict("USERNAME_EXISTS", "username already exists");
        }

        if (req.getEmail() != null && repo.existsByTenantIdAndEmail(tenantId, req.getEmail())) {
            throw BizException.conflict("EMAIL_EXISTS", "email already exists");
        }

        AppUser u = new AppUser();
        u.setTenantId(tenantId);
        u.setDeptId(req.getDeptId());
        u.setUsername(req.getUsername());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setDisplayName(req.getDisplayName());
        u.setEmail(req.getEmail());
        u.setStatus("ACTIVE");

        return toResp(repo.save(u));
    }

    public UserResponse update(Long id, UpdateUserRequest req) {
        Long tenantId = TenantContext.get();

        AppUser u = repo.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> BizException.notFound("USER_NOT_FOUND", "user not found"));

        if (req.getDisplayName() != null) u.setDisplayName(req.getDisplayName());
        if (req.getEmail() != null) u.setEmail(req.getEmail());
        if (req.getDeptId() != null) u.setDeptId(req.getDeptId());
        if (req.getStatus() != null) u.setStatus(req.getStatus());

        return toResp(repo.save(u));
    }

    public void disable(Long id) {
        Long tenantId = TenantContext.get();

        AppUser u = repo.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> BizException.notFound("USER_NOT_FOUND", "user not found"));

        u.setStatus("DISABLED");
        repo.save(u);
    }

    private static UserResponse toResp(AppUser u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .displayName(u.getDisplayName())
                .email(u.getEmail())
                .status(u.getStatus())
                .deptId(u.getDeptId())
                .build();
    }
}
