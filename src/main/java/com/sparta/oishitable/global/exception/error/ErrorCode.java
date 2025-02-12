package com.sparta.oishitable.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    ;

    private final HttpStatus status;
    private final String message;
}
