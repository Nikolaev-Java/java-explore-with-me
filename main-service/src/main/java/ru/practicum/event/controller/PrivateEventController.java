package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentEventOwnerDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateUserEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAllUserEvents(@PathVariable long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return service.privateGetAllUserEvents(userId, PageRequest.of(from, size));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDto create(@PathVariable long userId, @Valid @RequestBody NewEventDto dto) {
        return service.privateCreateEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getByUserId(@PathVariable long userId, @PathVariable long eventId) {
        return service.privateGetEventByUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long userId, @PathVariable long eventId,
                               @Valid @RequestBody UpdateUserEventDto dto) {
        return service.privateUpdateEventById(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEvent(@PathVariable long userId, @PathVariable long eventId) {
        return service.privateGetRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsByEvent(@PathVariable long userId, @PathVariable long eventId,
                                                                @RequestBody EventRequestStatusUpdateRequest dto) {
        return service.privateUpdateRequestsByEventId(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/comment/{commentId}")
    public CommentEventOwnerDto getCommentsByEvent(@PathVariable long eventId,
                                                   @PathVariable long userId,
                                                   @PathVariable long commentId) {
        return service.privateGetCommentByEvent(eventId, userId, commentId);
    }

    @GetMapping("/{eventId}/comment")
    public List<CommentEventOwnerDto> getAllCommentsByEvent(@PathVariable long eventId,
                                                            @PathVariable long userId,
                                                            @RequestParam(defaultValue = "0") int from,
                                                            @RequestParam(defaultValue = "10") int size) {
        return service.privateGetAllCommentByEvent(eventId, userId, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}/comment/{commentId}")
    public CommentEventOwnerDto updateCommentByEvent(@PathVariable long eventId,
                                                     @PathVariable long userId,
                                                     @PathVariable long commentId,
                                                     @Valid @RequestBody CommentEventOwnerDto dto) {
        return service.privateUpdateCommentByEvent(eventId, userId, dto, commentId);
    }
}
