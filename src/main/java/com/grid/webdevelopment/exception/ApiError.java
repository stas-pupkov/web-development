package com.grid.webdevelopment.exception;

public interface ApiError {

    String getName();
    String getErrorCode();
    String getMessage();
    int getHttpStatusCode();

}
