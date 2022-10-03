package com.skaria.aws.data.exception;

import lombok.Getter;

@Getter
public class SecurityException extends RuntimeException {

    private String message;

    public SecurityException(String message) {
        super(message);
        this.message = message;
    }
}


