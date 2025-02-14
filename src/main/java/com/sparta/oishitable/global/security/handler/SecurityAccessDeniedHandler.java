package com.sparta.oishitable.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.exception.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        log.warn("Security Authorization error: Access denied");

        ErrorCode error = ErrorCode.AUTHORIZATION_EXCEPTION;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(error.getStatus(), error.getMessage())));
    }
}
