package com.ludens.assignment.post.exception;

import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;

public class PostException extends BusinessException {

    private PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static PostException notFound() {
        return new PostException(ErrorCode.POST_NOT_FOUND);
    }

    public static PostException forbidden() {
        return new PostException(ErrorCode.FORBIDDEN);
    }
}
