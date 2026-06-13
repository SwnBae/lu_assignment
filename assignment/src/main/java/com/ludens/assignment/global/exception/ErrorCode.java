package com.ludens.assignment.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 포스트입니다."),

    // Heart
    ALREADY_HEARTED(HttpStatus.CONFLICT, "H001", "이미 하트한 포스트입니다."),
    NOT_HEARTED(HttpStatus.NOT_FOUND, "H002", "하트하지 않은 포스트입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 유저입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 사용 중인 username입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U003", "username 또는 비밀번호가 올바르지 않습니다."),

    // Auth
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "만료된 토큰입니다."),

    // Common
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C002", "접근 권한이 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C003", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
