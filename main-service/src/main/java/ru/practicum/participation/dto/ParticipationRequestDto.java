package ru.practicum.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    private long id;
    private String created;
    private int event;
    private int requester;
    private String status;
}
