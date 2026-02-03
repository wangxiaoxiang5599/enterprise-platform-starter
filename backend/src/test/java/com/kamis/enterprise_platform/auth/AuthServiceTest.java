package com.kamis.enterprise_platform.auth;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.security.jwt.JwtUtil;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        TenantContext.set(1L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void loginReturnsTokenForActiveUser() {
        AppUser user = new AppUser();
        user.setId(10L);
        user.setTenantId(1L);
        user.setUsername("admin");
        user.setPasswordHash("hashed");
        user.setStatus("ACTIVE");

        when(userRepository.findByUsernameAndTenantId("admin", 1L))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Admin@123", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("admin", 1L, 10L)).thenReturn("jwt-token");

        LoginResponse response = authService.login(new LoginRequest("admin", "Admin@123"));

        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void loginRejectsInvalidPassword() {
        AppUser user = new AppUser();
        user.setId(10L);
        user.setTenantId(1L);
        user.setUsername("admin");
        user.setPasswordHash("hashed");
        user.setStatus("ACTIVE");

        when(userRepository.findByUsernameAndTenantId("admin", 1L))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "wrong")))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("Invalid username or password");
    }
}
