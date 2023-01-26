package com.school.school.utils;

import com.school.school.exceptions.ValidationException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;

public class PageableValidator {

    public static String ERROR_MESSAGE = "Неверные параметры сортировки, поля c именем %s не существует.";
    public static String currentError;
    /*
    метод проверяет есть ли такие поля в классе "Class<T> c" по которым запрошена сортировка в "Pageable p"
     */
    static public <T> boolean isSortValid( Class<T> c, Pageable p) {
        Sort sort = p.getSort();
        try {
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                Field field = c.getDeclaredField(property);
            }
            return true;
        } catch (NoSuchFieldException e) {
            String fieldName = e.getMessage();
            currentError = String.format( ERROR_MESSAGE, fieldName );
            return false;
        }
    }

    static public <T> void checkIsSortValid(Class<T> c, Pageable p) {
        Sort sort = p.getSort();
        try {
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                Field field = c.getDeclaredField(property);
            }
        } catch (NoSuchFieldException e) {
            String fieldName = e.getMessage();
            throw new ValidationException(fieldName, String.format( ERROR_MESSAGE, fieldName ));
        }
    }
}
