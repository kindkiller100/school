package com.school.school.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectScheduleValidator implements ConstraintValidator<CorrectSchedule, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str != null) {
            //проверяем нужный формат: 'день_недели,время_начала_занятия,продолжительность_занятия_в_минутах;...'
            return str.matches("[1-7],([01][0-9]|2[0-3]):[0-5][0-9],([3-9][0-9]|1[0-9][0-9]|2[01][0-9])" +
                    "(;[1-7],([01][0-9]|2[0-3]):[0-5][0-9],([3-9][0-9]|1[0-9][0-9]|2[01][0-9])){0,6}");
        } else {
            //объект = null это тоже корректное значение
            return true;
        }
    }
}
