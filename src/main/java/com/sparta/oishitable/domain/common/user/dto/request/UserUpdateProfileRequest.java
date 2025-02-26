package com.sparta.oishitable.domain.common.user.dto.request;

import jakarta.validation.constraints.Size;

import static com.sparta.oishitable.domain.common.auth.dto.AuthValidationMessage.*;

public record UserUpdateProfileRequest(
        @Size(
                min = NICKNAME_MIN,
                max = NICKNAME_MAX,
                message = NICKNAME_RANGE_MESSAGE
        )
        String nickname,

        @Size(
                max = INTRODUCE_MAX,
                message = INTRODUCE_RANGE_MESSAGE
        )
        String introduce,
        Long regionId
) {
}
