package com.kamis.enterprise_platform.auth;

import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.security.jwt.JwtUtil;
import com.kamis.enterprise_platform.tenant.TenantContext;
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
        Long tenantId = TenantContext.get(); // 目前先用 header/default，下一步我们会改成更严谨

        AppUser user = userRepo.findByUsernameAndTenantId(req.getUsername(), tenantId)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("User is not active");
        }

        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getTenantId(), user.getId());
        return new LoginResponse(token);
    }

}
