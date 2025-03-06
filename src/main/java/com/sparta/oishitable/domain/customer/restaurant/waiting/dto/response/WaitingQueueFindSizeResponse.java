package com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueFindSizeResponse(
        Long inRestaurantWaitingSize,
        Long takeOutWaitingSize
) {

    public static WaitingQueueFindSizeResponse from(Long inRestaurantWaitingSize, Long takeOutWaitingSize) {
        return WaitingQueueFindSizeResponse.builder()
                .inRestaurantWaitingSize(inRestaurantWaitingSize)
                .takeOutWaitingSize(takeOutWaitingSize)
                .build();
    }
}
