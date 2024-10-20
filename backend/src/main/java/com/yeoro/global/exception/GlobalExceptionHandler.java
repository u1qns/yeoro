package com.yeoro.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(createErrorResponse(errorCode));
    }

    public ErrorResponse createErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getStatus().toString(),
                errorCode.getMessage()
        );
    }
}
