package com.otz.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiException(String message) {
        super(message);
        this.statusCode = 400; // Default Bad Request
    }
}
