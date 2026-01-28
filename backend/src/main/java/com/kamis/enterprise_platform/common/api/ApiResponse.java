package com.kamis.enterprise_platform.common.api;

import java.time.Instant;

public record ApiResponse<T>(boolean success,
                             String code,
                             String message,
                             T data,
                             String traceId,
                             Instant timestamp){
    public static <T> ApiResponse<T> ok(T data, String traceId) {
        return new ApiResponse<>(true, "OK", "OK", data, traceId, Instant.now());
    }

    public static <T> ApiResponse<T> fail(String code, String message, String traceId) {
        return new ApiResponse<>(false, code, message, null, traceId, Instant.now());
    }

}
