package com.pp.reservo.infrastructure.exceptions.response;

public class ErrorEntity {
    private final String errorCode;
    private final String description;

    public ErrorEntity(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
