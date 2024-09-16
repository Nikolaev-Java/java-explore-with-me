package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.event.Location;
import ru.practicum.event.validator.EventDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BasicUpdateEventDto {
    @Size(min = 20, max = 2000, message = "The annotation must be between 20 and 2000 characters long")
    protected String annotation;
    protected Long category;
    @Size(min = 20, max = 7000, message = "The annotation must be between 20 and 2000 characters long")
    protected String description;
    @EventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    protected Location location;
    protected Boolean paid;
    @PositiveOrZero(message = "Participation limit must be positive")
    protected Integer participantLimit;
    protected Boolean requestModeration;
    @Size(min = 3, max = 120, message = "The title must be between 3 and 120 characters long")
    protected String title;
}
