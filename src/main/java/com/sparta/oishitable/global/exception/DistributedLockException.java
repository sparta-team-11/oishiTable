package com.sparta.oishitable.global.exception;

import com.sparta.oishitable.global.exception.error.ErrorCode;

public class DistributedLockException extends CustomRuntimeException {
    public DistributedLockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
