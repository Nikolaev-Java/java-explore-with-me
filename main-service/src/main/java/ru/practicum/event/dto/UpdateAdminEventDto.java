package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class UpdateAdminEventDto extends BasicUpdateEventDto {
    @JsonProperty(value = "stateAction")
    private StateActionAdmin stateAction;

    //    @JsonSetter("stateAction")
//    public void setStateAction(String stateAction) {
//        if (stateAction != null) {
//            this.stateAction = StateActionAdmin.valueOf(stateAction);
//        }
//    }
    public enum StateActionAdmin {
        PUBLISH_EVENT, REJECT_EVENT
    }
}
