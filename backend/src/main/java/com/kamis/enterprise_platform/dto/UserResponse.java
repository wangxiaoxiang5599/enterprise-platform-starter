package com.kamis.enterprise_platform.dto;

import lombok.Builder;

@Builder
public record UserResponse (
    Long id,
    String username,
    String displayName,
    String email,
    String status,
    Long deptId
) {}
