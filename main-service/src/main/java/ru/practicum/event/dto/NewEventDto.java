package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.Location;
import ru.practicum.event.validator.EventDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "The annotation must be between 20 and 2000 characters long")
    @NotBlank(message = "Annotation must not be blank")
    private String annotation;
    @NotNull(message = "Id category must not be null")
    private Long category;
    @Size(min = 20, max = 7000, message = "The annotation must be between 20 and 2000 characters long")
    @NotBlank(message = "Description must not be blank")
    private String description;
    @EventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    @Builder.Default
    private boolean paid = false;
    @Builder.Default
    @PositiveOrZero(message = "Participation limit must be positive")
    private int participantLimit = 0;
    @Builder.Default
    private boolean requestModeration = true;
    @Size(min = 3, max = 120, message = "The title must be between 3 and 120 characters long")
    @NotBlank(message = "The title must not be blank")
    private String title;
}
