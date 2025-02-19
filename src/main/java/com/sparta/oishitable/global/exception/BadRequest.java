package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class BadRequest extends CustomRuntimeException {
    public BadRequest(ErrorCode errorCode) {
        super(errorCode);
    }
}
