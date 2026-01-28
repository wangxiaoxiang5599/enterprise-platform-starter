package com.kamis.enterprise_platform.common.api;

import com.kamis.enterprise_platform.common.trace.TraceFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private String traceId() {
        return MDC.get(TraceFilter.TRACE_ID);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse<Void>> handleBiz(BizException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.fail(e.getCode(), e.getMessage(), traceId()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("VALIDATION_ERROR", msg, traceId()));
    }

    // 未登录（认证失败）
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthenticationException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.fail("UNAUTHORIZED", "Unauthorized", traceId()));
    }

    // 无权限
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleDenied(AccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.fail("FORBIDDEN", "Forbidden", traceId()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e, HttpServletRequest req) {
        // 生产别把 e.getMessage() 直接给前端，避免泄露
        log.error("Unhandled exception", e);
        return ResponseEntity.status(500)
                .body(ApiResponse.fail("INTERNAL_ERROR", "Internal server error", traceId()));
    }
}
