package com.ludens.assignment.auth.exception;

import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;

public class AuthException extends BusinessException {

    private AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static AuthException invalidToken() {
        return new AuthException(ErrorCode.INVALID_TOKEN);
    }

    public static AuthException expiredToken() {
        return new AuthException(ErrorCode.EXPIRED_TOKEN);
    }
}
