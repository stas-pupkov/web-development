package com.grid.webdevelopment.exception;

public class ApiErrorBase implements ApiError {

    private final String name;
    private final String errorCode;
    private final String message;
    private final int httpStatusCode;

    public ApiErrorBase(String name, String errorCode, String message, int httpStatusCode) {
        if (name == null || errorCode == null) throw new IllegalArgumentException("Params can't be null");
        this.name = name;
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
