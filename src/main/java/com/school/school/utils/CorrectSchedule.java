package com.school.school.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectScheduleValidator.class)
@Documented
public @interface CorrectSchedule {
    String message() default "{Schedule.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
