package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.comment.StatusComment;
import ru.practicum.event.dto.EventShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentFullDto {
    private long id;
    private String text;
    private EventShortDto event;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comment;
    private StatusComment status;
}
