package com.sparta.oishitable.domain.common.auth.dto.request;

public record AccessTokenReissueReq(
        String accessToken,
        String refreshToken
) {
}
