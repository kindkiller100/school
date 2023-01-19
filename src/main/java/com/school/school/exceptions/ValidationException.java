package com.school.school.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    //ключ - название поля, значение - сообщение об ошибке, связанное с этим полем
    private Map<String, String> errors = new HashMap<>();

    public void put(String fieldName, String errorMessage) {
        this.errors.put(fieldName, errorMessage);
    }

    public Map<String, String> get() {
        return this.errors;
    }

    public void throwExceptionIfIsNotEmpty() {
        if(!this.errors.isEmpty()) {
            throw this;
        }
    }
}
