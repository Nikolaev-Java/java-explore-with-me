package ru.practicum.event.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDate, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }
        Duration duration = Duration.between(LocalDateTime.now(), localDateTime);
        return duration.toHours() >= 2;
    }
}
