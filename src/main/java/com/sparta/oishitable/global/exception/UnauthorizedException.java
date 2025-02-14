package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class UnauthorizedException extends CustomRuntimeException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
