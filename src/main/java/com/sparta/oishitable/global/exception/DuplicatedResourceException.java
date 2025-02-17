package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class DuplicatedResourceException extends CustomRuntimeException {
    public DuplicatedResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
