package com.ludens.assignment.global.exception;

import com.ludens.assignment.global.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return buildValidationResponse(message);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String message = e.getAllValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return buildValidationResponse(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception [{}]: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    private ResponseEntity<ErrorResponse> buildValidationResponse(String message) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode.getCode(), message));
    }
}
