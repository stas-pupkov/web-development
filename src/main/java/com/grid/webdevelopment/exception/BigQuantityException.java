package com.grid.webdevelopment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BigQuantityException extends RuntimeException {

    public BigQuantityException(String message) {
        super(message);
    }

}
