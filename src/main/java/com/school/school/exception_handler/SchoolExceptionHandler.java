package com.school.school.exception_handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class SchoolExceptionHandler extends ResponseEntityExceptionHandler {

    //обработка ошибок со статусом 400 и 404
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException exception) {
        HttpStatus status = exception instanceof NotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(exception.getMessage());
    }
    //обработка ошибок со статусом 500
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(SchoolValidationException.class)
    protected ResponseEntity<Object> handleSchoolValidationException(SchoolValidationException exception) {
        return new ResponseEntity(exception.getErrors(), HttpStatus.BAD_REQUEST);
    }

    //обработка исключений вызванных валидациями сущностей, возвращает список ошибок со статусом 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(errors, headers, status);
    }
}
