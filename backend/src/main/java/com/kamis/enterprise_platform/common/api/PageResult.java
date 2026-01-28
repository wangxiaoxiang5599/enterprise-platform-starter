package com.kamis.enterprise_platform.common.api;

public record PageResult( java.util.List content,
                          int page,          // 0-based（和 Spring 一致），如果你想 1-based 我也给你改法
                          int size,
                          long total,
                          int totalPages) {
}
