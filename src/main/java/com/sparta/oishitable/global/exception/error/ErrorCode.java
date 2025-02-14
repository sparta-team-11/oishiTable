package com.sparta.oishitable.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 식당 관련 익셉션
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 식당 입니다."),

    // SeatType 관련 익셉션
    SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 입니다."),

    // 팔로우 관련 익셉션
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우 할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 중인 관계입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
