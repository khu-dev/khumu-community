package com.khumu.community.application.exception;

import lombok.Getter;

@Getter
public class BaseKhumuException extends RuntimeException {
    public BaseKhumuException() {
        super();
    }

    public BaseKhumuException(String message) {
        super(message);
    }

    public BaseKhumuException(Throwable cause) {
        super(cause);
    }

    public BaseKhumuException(String message, Throwable cause) {
        super(message, cause);
    }
}