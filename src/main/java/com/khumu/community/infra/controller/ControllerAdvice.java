package com.khumu.community.infra.controller;

import com.khumu.community.application.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyAdvice {
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<DefaultResponse<Void>> forbiddenException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(DefaultResponse.<Void>builder()
                .message("작업을 수행할 권한이 없습니다. (" + e.getMessage() + ")").error(e.getClass().getSimpleName())
                .build());
    }
}
