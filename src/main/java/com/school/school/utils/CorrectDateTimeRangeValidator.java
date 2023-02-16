package com.school.school.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectDateTimeRangeValidator implements ConstraintValidator<CorrectDateTimeRange, DateRange> {
    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext constraintValidatorContext) {
        if (dateRange != null){
            return (dateRange.getFrom() != null) & (dateRange.getTo() != null);
        }
        return false;
    }
}
