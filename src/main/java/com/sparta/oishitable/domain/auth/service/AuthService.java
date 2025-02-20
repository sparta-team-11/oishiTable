package com.sparta.oishitable.domain.auth.service;

import com.sparta.oishitable.domain.auth.dto.request.AccessTokenReissueReq;
import com.sparta.oishitable.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.oishitable.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.oishitable.domain.auth.dto.response.AuthLoginResponse;
import com.sparta.oishitable.domain.auth.dto.response.AuthSignupResponse;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.DuplicatedResourceException;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.InvalidException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.security.JwtTokenProvider;
import com.sparta.oishitable.global.security.enums.TokenType;
import com.sparta.oishitable.global.security.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final long DURATION = Duration.ofDays(3).toMillis();

    @Transactional
    public AuthSignupResponse signup(AuthSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicatedResourceException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = request.toEntity(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return AuthSignupResponse.from(savedUser);
    }

    @Transactional
    public AuthLoginResponse signin(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidException(ErrorCode.INVALID_PASSWORD);
        }

        String userId = String.valueOf(user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        redisRepository.setDataWithExpire(userId, refreshToken, DURATION);

        return AuthLoginResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public AuthLoginResponse recreateAccessAndRefreshToken(AccessTokenReissueReq accessTokenReissueReq) {
        String accessToken = accessTokenReissueReq.accessToken();
        String refreshToken = accessTokenReissueReq.refreshToken();
        User user = jwtTokenProvider.getUserFromToken(accessToken);

        String userId = String.valueOf(user.getId());

        if (!jwtTokenProvider.validateToken(refreshToken, TokenType.REFRESH)) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!Objects.equals(refreshToken, redisRepository.getData(userId))) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole().getValue());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();

        redisRepository.setDataWithExpire(userId, newRefreshToken, DURATION);

        return AuthLoginResponse.of(newAccessToken, newRefreshToken);
    }

    public void checkAuthenticationUser(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
