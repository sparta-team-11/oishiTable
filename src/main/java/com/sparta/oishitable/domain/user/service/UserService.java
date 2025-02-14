package com.sparta.oishitable.domain.user.service;

import com.sparta.oishitable.domain.user.dto.request.UserSigninRequest;
import com.sparta.oishitable.domain.user.dto.request.UserSignupRequest;
import com.sparta.oishitable.domain.user.dto.response.UserSigninResponse;
import com.sparta.oishitable.domain.user.dto.response.UserSignupResponse;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance(); // 빈 등록전 임시로 지정

    @Transactional
    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomRuntimeException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = request.toEntity(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return UserSignupResponse.from(savedUser);
    }

    public UserSigninResponse signin(UserSigninRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomRuntimeException(ErrorCode.INVALID_PASSWORD);
        }

//        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname(), user.getUserRole()); jwt 토큰 생성 임시 제거

        return UserSigninResponse.from(null, "Bearer"); // 임시 null 반환
    }
}
