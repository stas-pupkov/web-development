package com.grid.webdevelopment.exception;

import java.util.UUID;

public enum ProjectApiError implements ApiError {

    USER_EXISTS("USER_EXISTS", "User exist already", 409);

    private final ApiError apiError;

    ProjectApiError(ApiError apiError) {
        this.apiError = apiError;
    }

    ProjectApiError(Object errorCode, String message, int httpStatusCode) {
        this(new ApiErrorBase("error-" + UUID.randomUUID(), String.valueOf(errorCode), message, httpStatusCode));
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getErrorCode() {
        return apiError.getErrorCode();
    }

    @Override
    public String getMessage() {
        return apiError.getMessage();
    }

    @Override
    public int getHttpStatusCode() {
        return apiError.getHttpStatusCode();
    }
}
