package com.school.school.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectDateTimeRangeValidator.class)
@Documented
public @interface CorrectDateTimeRange {
    String message() default "{CorrectDateTimeRange.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
