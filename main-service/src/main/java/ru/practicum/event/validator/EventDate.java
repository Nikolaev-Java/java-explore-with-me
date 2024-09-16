package ru.practicum.event.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateValidator.class)
@Documented
public @interface EventDate {
    String message() default "The date of the event should be no earlier than 2 hours later";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
