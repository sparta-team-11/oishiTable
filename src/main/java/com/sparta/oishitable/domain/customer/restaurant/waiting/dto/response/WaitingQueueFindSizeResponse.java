package com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueFindSizeResponse(
        Long size
) {

    public static WaitingQueueFindSizeResponse from(Long size) {
        return WaitingQueueFindSizeResponse.builder()
                .size(size)
                .build();
    }
}
