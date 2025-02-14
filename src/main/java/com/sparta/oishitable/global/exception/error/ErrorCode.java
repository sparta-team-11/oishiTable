package com.sparta.oishitable.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다."),

    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다"),

    // 식당 관련 익셉션
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 식당 입니다."),

    // 지역 관련 익셉션
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역 입니다."),

    // 게시글 관련 익셉션
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 입니다."),

    // 댓글 관련 익셉션
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글 입니다."),

    // 좋아요 관련 익셉션
    LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다."),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누르지 않으셨습니다");


    private final HttpStatus status;
    private final String message;
}
