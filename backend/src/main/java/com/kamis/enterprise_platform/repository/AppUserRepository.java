package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.AppUser;
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
}
