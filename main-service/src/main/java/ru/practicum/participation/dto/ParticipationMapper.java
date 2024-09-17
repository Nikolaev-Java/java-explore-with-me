package ru.practicum.participation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.participation.Participation;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ParticipationMapper {
    @Mapping(target = "event", source = "participation.event.id")
    @Mapping(target = "requester", source = "participation.user.id")
    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ParticipationRequestDto toParticipationRequestDto(Participation participation);

    @Mapping(target = "event", source = "participation.event.id")
    @Mapping(target = "requester", source = "participation.user.id")
    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    List<ParticipationRequestDto> toParticipationRequestDtoList(List<Participation> participations);
}
