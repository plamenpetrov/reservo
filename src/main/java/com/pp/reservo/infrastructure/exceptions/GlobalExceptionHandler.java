package com.pp.reservo.infrastructure.exceptions;

import com.pp.reservo.infrastructure.exceptions.response.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String VALIDATION_ERROR = "Validation error";
    public static final String VALIDATION_ERROR_DESCRIPTION = "Provided payload is invalid";
    private static final String NOT_FOUND_ERROR = "Entity not found";
    private static final String NOT_FOUND_DESCRIPTION_ERROR = "Entity does not exists";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        Map<String, List<String>> errorList = new HashMap<>();

        result.getFieldErrors().forEach((fieldError) -> {
            errorList.put(fieldError.getField(), addMessage(fieldError.getDefaultMessage()));
        });
        result.getGlobalErrors().forEach((fieldError) -> {
            errorList.put(fieldError.getObjectName(), addMessage(fieldError.getDefaultMessage()));
        });

        return new ResponseEntity<>(new ValidationErrors(VALIDATION_ERROR, VALIDATION_ERROR_DESCRIPTION, errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class, IllegalArgumentException.class })
    public ResponseEntity handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, List<String>> errorList = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldPath = violation.getPropertyPath().toString();
            String[] field = fieldPath.split("\\.");

            errorList.put(field[field.length - 1], addMessage(violation.getMessageTemplate()));
        }

        return new ResponseEntity<>(new ValidationErrors(VALIDATION_ERROR, VALIDATION_ERROR_DESCRIPTION, errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        Map<String, List<String>> errorList = new HashMap<>();

        errorList.put("error", Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(new ValidationErrors(NOT_FOUND_ERROR, NOT_FOUND_DESCRIPTION_ERROR, errorList), HttpStatus.BAD_REQUEST);
    }

    private List<String> addMessage(String exception) {
        List<String> message = new ArrayList<>();
        message.add(exception);

        return message;
    }
}
