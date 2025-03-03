package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class ForbiddenException extends CustomRuntimeException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
