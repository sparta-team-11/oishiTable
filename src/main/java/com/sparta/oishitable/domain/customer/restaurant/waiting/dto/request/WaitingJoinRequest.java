package com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaitingJoinRequest(
        @NotNull
        Integer totalCount,

        @NotBlank
        String waitingType
) {
}
