package com.kamis.enterprise_platform.service;

import com.kamis.enterprise_platform.common.api.BizException;
import com.kamis.enterprise_platform.dto.CreateUserRequest;
import com.kamis.enterprise_platform.dto.UpdateUserRequest;
import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        TenantContext.set(1L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createUserPersistsTenantScopedUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("new-user");
        request.setPassword("Secret123");
        request.setDisplayName("New User");
        request.setEmail("new@example.com");
        request.setDeptId(7L);

        when(repository.existsByTenantIdAndUsername(1L, "new-user")).thenReturn(false);
        when(repository.existsByTenantIdAndEmail(1L, "new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret123")).thenReturn("hashed");

        AppUser saved = new AppUser();
        saved.setId(99L);
        saved.setTenantId(1L);
        saved.setUsername("new-user");
        saved.setPasswordHash("hashed");
        saved.setDisplayName("New User");
        saved.setEmail("new@example.com");
        saved.setDeptId(7L);
        saved.setStatus("ACTIVE");

        when(repository.save(org.mockito.Mockito.any(AppUser.class))).thenReturn(saved);

        userService.create(request);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());
        AppUser created = captor.getValue();
        assertThat(created.getTenantId()).isEqualTo(1L);
        assertThat(created.getUsername()).isEqualTo("new-user");
        assertThat(created.getPasswordHash()).isEqualTo("hashed");
        assertThat(created.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void createUserRejectsDuplicateUsername() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("existing");
        request.setPassword("Secret123");

        when(repository.existsByTenantIdAndUsername(1L, "existing")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("username already exists");
    }

    @Test
    void updateUserAppliesChanges() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Updated");
        request.setEmail("updated@example.com");
        request.setDeptId(9L);
        request.setStatus("DISABLED");

        AppUser existing = new AppUser();
        existing.setId(5L);
        existing.setTenantId(1L);
        existing.setUsername("user");
        existing.setStatus("ACTIVE");

        when(repository.findByTenantIdAndId(1L, 5L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        userService.update(5L, request);

        assertThat(existing.getDisplayName()).isEqualTo("Updated");
        assertThat(existing.getEmail()).isEqualTo("updated@example.com");
        assertThat(existing.getDeptId()).isEqualTo(9L);
        assertThat(existing.getStatus()).isEqualTo("DISABLED");
    }
}
