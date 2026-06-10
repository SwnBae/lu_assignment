package com.ludens.assignment.user.exception;

import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;

public class UserException extends BusinessException {

    private UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static UserException notFound() {
        return new UserException(ErrorCode.USER_NOT_FOUND);
    }

    public static UserException usernameAlreadyExists() {
        return new UserException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }

    public static UserException invalidCredentials() {
        return new UserException(ErrorCode.INVALID_CREDENTIALS);
    }
}
