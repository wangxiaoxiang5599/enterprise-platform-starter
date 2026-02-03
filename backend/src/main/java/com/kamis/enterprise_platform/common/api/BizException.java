package com.kamis.enterprise_platform.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class BizException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public BizException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() { return code; }
    public HttpStatus getStatus() { return status; }

    public static BizException conflict(String code, String message) {
        log.warn("Conflict: " + message);
        return new BizException(code, message, HttpStatus.CONFLICT);
    }

    public static BizException badRequest(String code, String message) {
        log.warn("Bad request: " + message);
        return new BizException(code, message, HttpStatus.BAD_REQUEST);
    }

    public static BizException notFound(String code, String message) {
        log.warn("Not found: " + message);
        return new BizException(code, message, HttpStatus.NOT_FOUND);
    }

    public static BizException forbidden(String code, String message) {
        return new BizException(code, message, HttpStatus.FORBIDDEN);
    }
}
