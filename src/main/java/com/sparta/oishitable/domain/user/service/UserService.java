package com.sparta.oishitable.domain.user.service;

import com.sparta.oishitable.domain.user.dto.request.UserSignupRequest;
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
}
