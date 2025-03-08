package com.sparta.oishitable.domain.owner.restaurant.waiting.entity;

import com.sparta.oishitable.global.exception.BadRequest;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WaitingType {
    IN("waiting:in:"),
    OUT("waiting:out:");

    private final String prefix;

    public static WaitingType of(String waitingType) {
        return Arrays.stream(WaitingType.values())
                .filter(w -> w.name().equalsIgnoreCase(waitingType))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_WAITING_TYPE));
    }

    public String getWaitingKey(Long restaurantId) {
        return this.prefix + "queue:" + restaurantId;
    }

    public String getSequenceKey(Long restaurantId) {
        return this.prefix + "sequence:" + restaurantId;
    }
}

