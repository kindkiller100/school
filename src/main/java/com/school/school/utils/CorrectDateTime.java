package com.school.school.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectDateTimeValidator.class)
@Documented
public @interface CorrectDateTime {
    String message() default "{CorrectDateTime.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
