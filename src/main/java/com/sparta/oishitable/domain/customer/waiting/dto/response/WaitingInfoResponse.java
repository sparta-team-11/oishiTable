package com.sparta.oishitable.domain.customer.waiting.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingStatus;

import java.time.LocalDateTime;

public record WaitingInfoResponse(
        Long waitingId,
        Long userId,
        Long restaurantId,
        Integer totalCount,
        Integer dailySequence,
        WaitingStatus status,
        String restaurantName,
        LocalDateTime createdAt
) {

    @QueryProjection
    public WaitingInfoResponse {
    }
}
