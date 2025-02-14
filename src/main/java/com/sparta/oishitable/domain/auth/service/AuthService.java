package com.sparta.oishitable.domain.auth.service;

import com.sparta.oishitable.domain.auth.dto.request.AccessTokenReissueReq;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.global.exception.InvalidException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.security.JwtTokenProvider;
import com.sparta.oishitable.global.security.dto.response.AuthLoginResponse;
import com.sparta.oishitable.global.security.enums.TokenType;
import com.sparta.oishitable.global.security.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisUtil;

    private final long DURATION = Duration.ofDays(3).toMillis();

    @Transactional
    public AuthLoginResponse recreateAccessAndRefreshToken(AccessTokenReissueReq accessTokenReissueReq) {
        String accessToken = accessTokenReissueReq.accessToken();
        String refreshToken = accessTokenReissueReq.refreshToken();
        User user = jwtTokenProvider.getUserFromToken(accessToken);

        String userId = String.valueOf(user.getId());

        if (!jwtTokenProvider.validateToken(accessTokenReissueReq.refreshToken(), TokenType.REFRESH) ||
                !redisUtil.getData(userId).equals(refreshToken)) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();

        redisUtil.setDataWithExpire(userId, newRefreshToken, DURATION);

        return new AuthLoginResponse(newAccessToken, newRefreshToken);
    }
}
