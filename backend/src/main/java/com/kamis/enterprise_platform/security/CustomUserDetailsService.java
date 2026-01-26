package com.kamis.enterprise_platform.security;

import com.kamis.enterprise_platform.entity.AppUser;
import com.kamis.enterprise_platform.repository.PermissionRepository;
import com.kamis.enterprise_platform.repository.AppUserRepository;
import com.kamis.enterprise_platform.tenant.TenantContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public CustomUserDetailsService(AppUserRepository userRepository,
                                    PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long tenantId = TenantContext.get();

        AppUser user = userRepository
                .findByUsernameAndTenantId(username, tenantId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> perms =
                permissionRepository.findPermissionCodesByUserId(user.getId());

        List<SimpleGrantedAuthority> authorities = perms.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountLocked(!"ACTIVE".equals(user.getStatus()))
                .build();
    }
}
