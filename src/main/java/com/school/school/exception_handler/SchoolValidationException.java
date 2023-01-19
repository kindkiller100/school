package com.school.school.exception_handler;

import java.util.HashMap;
import java.util.Map;

public class SchoolValidationException extends RuntimeException {
    //ключ - название поля, значение - сообщение об ошибке, связанное с этим полем
    private Map<String, String> errors = new HashMap<>();

    public void addError(String fieldName, String errorMessage) {
        this.errors.put(fieldName, errorMessage);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
