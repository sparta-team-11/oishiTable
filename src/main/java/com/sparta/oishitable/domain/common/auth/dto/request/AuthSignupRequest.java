package com.sparta.oishitable.domain.common.auth.dto.request;

import com.sparta.oishitable.domain.common.auth.dto.AuthValidationMessage;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthSignupRequest(
        @NotBlank(message = AuthValidationMessage.EMAIL_BLANK_MESSAGE)
        @Pattern(
                regexp = AuthValidationMessage.EMAIL_REG,
                message = AuthValidationMessage.INVALID_EMAIL_MESSAGE
        )
        String email,

        @NotBlank(message = AuthValidationMessage.PASSWORD_BLANK_MESSAGE)
        @Size(
                min = AuthValidationMessage.PASSWORD_MIN,
                max = AuthValidationMessage.PASSWORD_MAX,
                message = AuthValidationMessage.PASSWORD_RANGE_MESSAGE)
        @Pattern(
                regexp = AuthValidationMessage.PASSWORD_REG,
                message = AuthValidationMessage.INVALID_PASSWORD_MESSAGE
        )
        String password,

        @NotBlank(message = AuthValidationMessage.NICKNAME_BLANK_MESSAGE)
        @Size(
                min = AuthValidationMessage.NICKNAME_MIN,
                max = AuthValidationMessage.NICKNAME_MAX,
                message = AuthValidationMessage.NICKNAME_RANGE_MESSAGE
        )
        String name,

        @NotBlank(message = AuthValidationMessage.PHONE_NUMBER_BLANK_MESSAGE)
        @Pattern(
                regexp = AuthValidationMessage.PHONE_NUMBER_REG,
                message = AuthValidationMessage.INVALID_PHONE_NUMBER_MESSAGE
        )
        String phoneNumber,

        @NotNull(message = AuthValidationMessage.ROLE_NULL_MESSAGE)
        UserRole userRole
) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .phoneNumber(phoneNumber)
                .role(userRole)
                .build();
    }
}