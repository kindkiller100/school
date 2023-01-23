package com.school.school.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectDateTimeRangeValidator implements ConstraintValidator<CorrectDateTimeRange, DateTimeRange> {
    @Override
    public boolean isValid(DateTimeRange dateTimeRange, ConstraintValidatorContext constraintValidatorContext) {
        if (dateTimeRange != null){
            return (dateTimeRange.getFrom() != null) & (dateTimeRange.getTo() != null);
        }
        return false;
    }
}
