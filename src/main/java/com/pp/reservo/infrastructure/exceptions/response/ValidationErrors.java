package com.pp.reservo.infrastructure.exceptions.response;

import java.util.List;
import java.util.Map;

public class ValidationErrors extends ErrorEntity {

    private final Map<String, List<String>> validationErrors;

    public ValidationErrors(String errorCode, String description, Map<String, List<String>> errors) {
        super(errorCode, description);
        this.validationErrors = errors;
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }
}
