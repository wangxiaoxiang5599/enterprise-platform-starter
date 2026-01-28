package com.kamis.enterprise_platform.common.api;

import com.kamis.enterprise_platform.common.trace.TraceFilter;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 已经是 ApiResponse 的就不再包一次
        return !ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // /error 这种避免二次包装（保险）
        if (request instanceof ServletServerHttpRequest r) {
            String path = r.getServletRequest().getRequestURI();
            if ("/error".equals(path) || path.startsWith("/health")) return body;
        }

        // 如果运行时已经是 ApiResponse，就直接返回，避免嵌套
        if (body instanceof ApiResponse) {
            return body;
        }

        String traceId = MDC.get(TraceFilter.TRACE_ID);

        // 如果是 Page，转换成 PageResult 再返回
        if (body instanceof Page<?> page) {
            var result = new PageResult(
                    page.getContent(),
                    page.getNumber() + 1,
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );
            return ApiResponse.ok(result, traceId);
        }

        return ApiResponse.ok(body, traceId);
    }
}
