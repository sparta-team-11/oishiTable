package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
