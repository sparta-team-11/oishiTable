package com.sparta.oishitable.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션



    // 식당 관련 익셉션
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 식당 입니다."),

    // SeatType 관련 익셉션
    SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
