package com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;

public record WaitingDetails(
        Long waitingId,
        Long userId,
        String userName,
        Integer totalCount,
        String userPhoneNumber,
        ReservationStatus status,
        WaitingType type
) {

    @QueryProjection
    public WaitingDetails {
    }
}
