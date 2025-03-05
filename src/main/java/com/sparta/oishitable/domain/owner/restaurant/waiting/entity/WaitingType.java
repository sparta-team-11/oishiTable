package com.sparta.oishitable.domain.owner.restaurant.waiting.entity;

import com.sparta.oishitable.global.exception.BadRequest;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WaitingType {
    IN("in_restaurant_waiting_queue:"),
    OUT("take_out_waiting_queue:");

    private final String prefix;

    public static WaitingType of(String waitingType) {
        return Arrays.stream(WaitingType.values())
                .filter(w -> w.name().equalsIgnoreCase(waitingType))
                .findFirst()
                .orElseThrow(() -> new BadRequest(ErrorCode.INVALID_WAITING_TYPE));
    }
}

