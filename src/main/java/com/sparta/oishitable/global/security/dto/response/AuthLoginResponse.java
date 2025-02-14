package com.sparta.oishitable.global.security.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AuthLoginResponse(
        String accessToken,
        String refreshToken
) {

    public static AuthLoginResponse of(String accessToken, String refreshToken) {
        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
