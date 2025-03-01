package com.sparta.oishitable.domain.owner.restaurant.waiting.entity;

import com.sparta.oishitable.global.exception.BadRequest;
import com.sparta.oishitable.global.exception.error.ErrorCode;

import java.util.Arrays;

public enum WaitingType {
    IN, OUT;

    public static WaitingType of(String waitingType) {
        return Arrays.stream(WaitingType.values())
                .filter(w -> w.name().equalsIgnoreCase(waitingType))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_WAITING_TYPE));
    }
}

