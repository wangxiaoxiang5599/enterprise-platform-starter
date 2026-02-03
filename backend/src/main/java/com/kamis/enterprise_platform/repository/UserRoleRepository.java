package com.kamis.enterprise_platform.repository;

import com.kamis.enterprise_platform.entity.UserRole;
import com.kamis.enterprise_platform.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    @Modifying
    @Query("delete from UserRole ur where ur.userId = :userId")
    void deleteByUserId(Long userId);

    @Query("select ur.roleId from UserRole ur where ur.userId = :userId")
    List<Long> findRoleIdsByUserId(Long userId);
}
