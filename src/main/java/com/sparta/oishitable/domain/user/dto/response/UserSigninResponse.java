package com.sparta.oishitable.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserSigninResponse(
        String accessToken,
        String tokenType
) {

    public static UserSigninResponse from(String accessToken, String tokenType) {
        return UserSigninResponse.builder()
                .accessToken(accessToken)
                .tokenType(tokenType)
                .build();
    }
}
