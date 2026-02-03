package com.kamis.enterprise_platform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private Long id;
    private String code;
    private String name;
    private String type;
    private Long parentId;
    private Integer sortOrder;
}
