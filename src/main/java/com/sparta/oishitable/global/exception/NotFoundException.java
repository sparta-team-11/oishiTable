package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
