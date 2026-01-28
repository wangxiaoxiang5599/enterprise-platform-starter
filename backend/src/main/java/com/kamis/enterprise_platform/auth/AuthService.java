package com.kamis.enterprise_platform.auth;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.security.jwt.JwtUtil;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(AppUserRepository userRepo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest req) {
        Long tenantId = TenantContext.get();

        AppUser user = userRepo.findByUsernameAndTenantId(req.getUsername(), tenantId)
                .orElseThrow(() ->
                        new BizException("AUTH_FAILED", "Invalid username or password", HttpStatus.UNAUTHORIZED)
                );

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new BizException("USER_DISABLED", "User is not active", HttpStatus.FORBIDDEN);
        }

        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BizException("AUTH_FAILED", "Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getTenantId(), user.getId());
        return new LoginResponse(token);
    }

}
