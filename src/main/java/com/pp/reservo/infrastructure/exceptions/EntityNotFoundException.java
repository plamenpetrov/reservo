package com.pp.reservo.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity was not found")
public class EntityNotFoundException extends RuntimeException {

    private int statusCode;

    public EntityNotFoundException() {
        this.setStatusCode(404);
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.setStatusCode(404);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
