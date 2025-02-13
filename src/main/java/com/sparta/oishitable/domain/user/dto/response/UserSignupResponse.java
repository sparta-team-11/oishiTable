package com.sparta.oishitable.domain.user.dto.response;

import com.sparta.oishitable.domain.user.entity.User;

public record UserSignupResponse(
        String email,
        String name
) {
    public static UserSignupResponse from(User user) {
        return new UserSignupResponse(user.getEmail(), user.getName());
    }
}
