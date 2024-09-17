package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.comment.StatusComment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEventOwnerDto {
    private long id;
    private long user;
    private String text;
    @Size(min = 2, max = 1000)
    private String comment;
    @JsonProperty(value = "status")
    private StatusComment status;
}
