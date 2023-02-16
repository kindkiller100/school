package com.school.school.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    //ключ - название поля, значение - сообщение об ошибке, связанное с этим полем
    private Map<String, String> errors = new HashMap<>();
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public ValidationException() {
    }

    public ValidationException(String fieldName, String errorMessage) {
        this.put(fieldName, errorMessage);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ValidationException setStatus(HttpStatus status) {
        this.status = status;

        return this;
    }

    public Map<String, String> get() {
        return this.errors;
    }

    public void put(String fieldName, String errorMessage) {
        this.errors.put(fieldName, errorMessage);
    }

    public void put(ValidationException exception) {
        this.errors.putAll(exception.get());

    }

    public void throwExceptionIfIsNotEmpty() {
        if(!this.errors.isEmpty()) {
            throw this;
        }
    }
}
