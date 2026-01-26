package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = """
      select p.code
      from permission p
      join role_permission rp on p.id = rp.permission_id
      join role r on r.id = rp.role_id
      join user_role ur on ur.role_id = r.id
      join app_user u on u.id = ur.user_id
      where u.id = :userId
    """, nativeQuery = true)
    List<String> findPermissionCodesByUserId(Long userId);
}
