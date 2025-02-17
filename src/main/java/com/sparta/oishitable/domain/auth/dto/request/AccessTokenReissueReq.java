package com.sparta.oishitable.domain.auth.dto.request;

public record AccessTokenReissueReq(
        String accessToken,
        String refreshToken
) {
}
