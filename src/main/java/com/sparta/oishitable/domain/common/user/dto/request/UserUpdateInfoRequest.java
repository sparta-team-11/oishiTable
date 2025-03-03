package com.sparta.oishitable.domain.common.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.sparta.oishitable.domain.common.auth.dto.AuthValidationMessage.*;

public record UserUpdateInfoRequest(
        @Size(
                min = NAME_MIN,
                max = NAME_MAX,
                message = NAME_RANGE_MESSAGE
        )
        String name,

        @Pattern(
                regexp = PHONE_NUMBER_REG,
                message = INVALID_PHONE_NUMBER_MESSAGE
        )
        String phoneNumber
) {
}
