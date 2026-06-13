package com.ludens.assignment.heart.exception;

import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;

public class HeartException extends BusinessException {

    private HeartException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static HeartException alreadyHearted() {
        return new HeartException(ErrorCode.ALREADY_HEARTED);
    }

    public static HeartException notHearted() {
        return new HeartException(ErrorCode.NOT_HEARTED);
    }
}
