package com.school.school.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CorrectDateTimeValidator implements ConstraintValidator<CorrectDateTime, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime != null) {
            return localDateTime.isAfter(LocalDateTime.now().minusDays(1));
        }
        return false;
    }
}
