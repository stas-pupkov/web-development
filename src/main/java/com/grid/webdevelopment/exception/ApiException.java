package com.grid.webdevelopment.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiException extends RuntimeException {

    private final List<ApiError> apiErrors;

    public ApiException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiErrors = new ArrayList<>(Collections.singletonList(apiError));
    }
}
