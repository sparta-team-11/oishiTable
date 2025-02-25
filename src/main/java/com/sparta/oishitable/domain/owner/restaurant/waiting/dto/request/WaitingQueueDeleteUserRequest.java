package com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request;

import jakarta.validation.constraints.NotNull;

public record WaitingQueueDeleteUserRequest(
        @NotNull
        Long userId
) {
}
