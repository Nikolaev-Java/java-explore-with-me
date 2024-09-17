package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateUserEventDto extends BasicUpdateEventDto {
    @JsonProperty(value = "stateAction")
    private StateActionUser stateAction;

    public enum StateActionUser {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }
}
