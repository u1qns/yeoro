package com.yeoro.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token이 유효하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "Refresh Token이 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
