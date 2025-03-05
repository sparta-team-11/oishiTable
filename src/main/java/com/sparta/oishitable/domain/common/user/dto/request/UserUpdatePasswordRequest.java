package com.sparta.oishitable.domain.common.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.sparta.oishitable.domain.common.auth.dto.AuthValidationMessage.*;
import static com.sparta.oishitable.domain.common.auth.dto.AuthValidationMessage.INVALID_PASSWORD_MESSAGE;

public record UserUpdatePasswordRequest(
        @NotBlank(message = PASSWORD_BLANK_MESSAGE)
        @Size(
                min = PASSWORD_MIN,
                max = PASSWORD_MAX,
                message = PASSWORD_RANGE_MESSAGE)
        @Pattern(
                regexp = PASSWORD_REG,
                message = INVALID_PASSWORD_MESSAGE
        )
        String password
) {
}
