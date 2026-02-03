package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByTenantId(Long tenantId);

    Optional<Role> findByTenantIdAndId(Long tenantId, Long id);

    boolean existsByTenantIdAndCode(Long tenantId, String code);
}
