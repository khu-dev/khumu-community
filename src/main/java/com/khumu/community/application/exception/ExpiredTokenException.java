package com.khumu.community.application.exception;

public class ExpiredTokenException extends BaseKhumuException{
    public ExpiredTokenException() {
        super();
    }

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(Throwable cause) {
        super(cause);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
