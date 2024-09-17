package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateAdminEventDto;
import ru.practicum.event.dto.UpdateUserEventDto;
import ru.practicum.event.utils.AdminEventsFilters;
import ru.practicum.event.utils.PublicEventsFilters;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    List<EventShortDto> privateGetAllUserEvents(long userId, PageRequest page);

    EventFullDto privateCreateEvent(long userId, NewEventDto dto);

    EventFullDto privateGetEventByUserId(long userId, long eventId);

    EventFullDto privateUpdateEventById(long userId, long eventId, UpdateUserEventDto dto);

    List<ParticipationRequestDto> privateGetRequestsByEventId(long userId, long eventId);

    EventRequestStatusUpdateResult privateUpdateRequestsByEventId(long userId, long eventId, EventRequestStatusUpdateRequest dto);

    List<EventFullDto> adminGetAllEvents(AdminEventsFilters filters, Pageable page);

    EventFullDto adminUpdateEvent(long eventId, UpdateAdminEventDto dto);

    List<EventShortDto> publicGetAllEvents(PublicEventsFilters publicEventsFilters, PageRequest page, HttpServletRequest request);

    EventFullDto publicGetEventById(long id, HttpServletRequest request);
}
