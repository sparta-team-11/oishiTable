package com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class WaitingUserDetails {
    private final Long userId;
    private final String name;
    private final String phoneNumber;

    @QueryProjection
    public WaitingUserDetails(Long userId, String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
