package com.sparta.oishitable.global.security.dto.request;

public record AuthLoginRequest(
        String email,
        String password
) {
}
