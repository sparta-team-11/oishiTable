package com.sparta.oishitable.domain.user.dto.response;

import com.sparta.oishitable.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserSignupResponse(
        String email,
        String name
) {

    public static UserSignupResponse from(User user) {
        return UserSignupResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
