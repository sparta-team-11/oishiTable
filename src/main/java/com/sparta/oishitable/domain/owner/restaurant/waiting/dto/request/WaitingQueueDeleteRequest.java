package com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WaitingQueueDeleteRequest(
        @NotBlank
        String waitingType
) {
}
