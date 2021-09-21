package com.grid.webdevelopment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductAddedAlreadyException extends RuntimeException {

    public ProductAddedAlreadyException(String message) {
        super(message);
    }

}
