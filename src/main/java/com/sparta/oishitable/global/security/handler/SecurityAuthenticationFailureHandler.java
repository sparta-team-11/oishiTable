package com.sparta.oishitable.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.exception.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        log.warn("login error: {}", exception.getMessage());

        ErrorCode errorCode = ErrorCode.LOGIN_FAILED_EXCEPTION;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(errorCode.getStatus(), exception.getMessage())));
    }
}
