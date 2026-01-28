package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("""
        select u from AppUser u
        where u.username = :username and u.tenantId = :tenantId
    """)
    Optional<AppUser> findByUsernameAndTenantId(
            String username, Long tenantId);

    Optional<AppUser> findByTenantIdAndId(Long tenantId, Long id);

    Page<AppUser> findByTenantId(Long tenantId, Pageable pageable);

    boolean existsByTenantIdAndUsername(Long tenantId, String username);

    boolean existsByTenantIdAndEmail(Long tenantId, String email);
}
