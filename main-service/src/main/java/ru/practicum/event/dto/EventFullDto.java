package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.Location;
import ru.practicum.event.State;
import ru.practicum.user.dto.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private long id;
    private UserShortDto initiator;
    private String annotation;
    private String createdOn;
    private CategoryDto category;
    private String description;
    private String eventDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publishedOn;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
    private State state;
    private int views;
    private int confirmedRequests;

}
