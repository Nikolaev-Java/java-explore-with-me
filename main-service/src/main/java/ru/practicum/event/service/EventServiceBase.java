package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ClientService;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.category.Category;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.Location;
import ru.practicum.event.LocationRepository;
import ru.practicum.event.QEvent;
import ru.practicum.event.State;
import ru.practicum.event.dto.BasicUpdateEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateAdminEventDto;
import ru.practicum.event.dto.UpdateUserEventDto;
import ru.practicum.event.utils.AdminEventsFilters;
import ru.practicum.event.utils.PublicEventsFilters;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.Participation;
import ru.practicum.participation.ParticipationRepository;
import ru.practicum.participation.Status;
import ru.practicum.participation.dto.ParticipationMapper;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceBase implements EventService {
    public static final String EVENT_NOT_FOUND_EXC = "Event not found with id ";
    public static final String APP = "ewm-main-service";
    private final EventRepository eventRepository;
    private final EventMapper mapper;
    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;
    private final ClientService clientService;
    private final LocationRepository locationRepository;

    @Override
    public List<EventShortDto> privateGetAllUserEvents(long userId, PageRequest page) {
        log.debug("Received request for private get all user events. User id: {}", userId);
        List<Event> allByInitiatorId = eventRepository.findAllByInitiatorId(userId, page);
        log.debug("Found all event by user. Size: {}", allByInitiatorId.size());
        return mapper.toShortDtoList(allByInitiatorId);
    }

    @Override
    public EventFullDto privateCreate(long userId, NewEventDto dto) {
        log.debug("Received request for private create event. User id: {}", userId);
        Event event = mapper.fromNewEventDto(dto);
        Location saved = locationRepository.save(event.getLocation());
        event.setInitiator(User.builder().id(userId).build());
        event.setState(State.PENDING);
        event.setLocation(saved);
        Event save = eventRepository.save(event);
        log.debug("Create event. Event id: {}", save.getId());
        return mapper.toEventFullDto(save);
    }

    @Override
    public EventFullDto privateGetByUser(long userId, long eventId) {
        log.debug("Received request for private get event by user. User id: {}", userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND_EXC + eventId));
        log.debug("Found event with id.{}", eventId);
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto privateUpdate(long userId, long eventId, UpdateUserEventDto dto) {
        log.debug("Received request for private update event. Event id: {}", eventId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_EXC + eventId));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Published event cannot be updated");
        }
        updateEvent(dto, event);
        updateStatusByUser(dto, event);
        Event save = eventRepository.save(event);
        log.debug("Update event. From Dto: {}", dto);
        return mapper.toEventFullDto(save);
    }

    @Override
    public List<ParticipationRequestDto> privateGetRequestsByEvent(long userId, long eventId) {
        log.debug("Received request for private get requests by event. Event id: {}", eventId);
        List<Participation> participations = participationRepository.findAllByEventId(eventId);
        List<ParticipationRequestDto> response = participationMapper.toParticipationRequestDtoList(participations);
        log.debug("Participation request response: {}", response);
        return response;
    }

    @Override
    public EventRequestStatusUpdateResult privateUpdateRequestsByEvent(long userId, long eventId, EventRequestStatusUpdateRequest dto) {
        log.debug("Received request for private update requests by event. Event id: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_EXC + eventId));
        List<Participation> requests = participationRepository.findByIdIn(dto.getRequestIds());
        boolean isConfirmedRequest = requests.stream().anyMatch(request -> request.getStatus().equals(Status.CONFIRMED));
        if (isConfirmedRequest && dto.getStatus().equals(EventRequestStatusUpdateRequest.Status.REJECTED)) {
            throw new ConflictException("Confirmed request cannot be reject");
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            requests.forEach(participation -> participation.setStatus(Status.CONFIRMED));
            event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(participationMapper.toParticipationRequestDtoList(requests)).build();
        }
        if (dto.getStatus().equals(EventRequestStatusUpdateRequest.Status.REJECTED)) {
            requests.forEach(participation -> participation.setStatus(Status.REJECTED));
            return EventRequestStatusUpdateResult.builder()
                    .rejectedRequests(participationMapper.toParticipationRequestDtoList(requests)).build();
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("Participation limit exceeded");
        }
        boolean eventNotPendingStatus = requests.stream()
                .anyMatch(participation -> !participation.getStatus().equals(Status.PENDING));
        if (eventNotPendingStatus) {
            throw new ConflictException("the status can only be changed for applications that are in the waiting state");
        }
        Status requestStatusUpdate = Status.valueOf(dto.getStatus().toString());
        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        List<Participation> confirmedRequestsList = new ArrayList<>();
        List<Participation> rejectedRequestsList = new ArrayList<>();
        for (Participation request : requests) {
            if (confirmedRequests == participantLimit) {
                request.setStatus(Status.REJECTED);
                rejectedRequestsList.add(request);
            } else {
                request.setStatus(requestStatusUpdate);
                confirmedRequestsList.add(request);
                confirmedRequests++;
            }
        }
        event.setConfirmedRequests(confirmedRequests);
        participationRepository.saveAll(requests);
        eventRepository.save(event);
        log.debug("Participation to ids {} update request status to: {}", dto.getRequestIds(), requestStatusUpdate);
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(participationMapper.toParticipationRequestDtoList(rejectedRequestsList))
                .confirmedRequests(participationMapper.toParticipationRequestDtoList(confirmedRequestsList)).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> adminGetAllEvents(AdminEventsFilters filters, Pageable page) {
        log.debug("Received request for admin get all events. Filters: {}", filters);
        List<Event> events;
        if (Objects.nonNull(filters.getPredicate())) {
            events = eventRepository.findAll(filters.getPredicate(), page).getContent();
        } else {
            events = eventRepository.findAll(page).getContent();
        }
        log.debug("Found events. Size: {}", events.size());
        return mapper.toEventFullDtoList(events);
    }

    @Override
    public EventFullDto adminUpdateEvent(long eventId, UpdateAdminEventDto dto) {
        log.debug("Received request for admin update events id: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
        Duration duration = Duration.between(LocalDateTime.now(), event.getEventDate());
        if (duration.toHours() < 1) {
            throw new ConflictException("the start date of the event to be modified must " +
                                        "be no earlier than one hour from the date of publication.");
        }
        if (Objects.nonNull(dto.getStateAction())
            && dto.getStateAction().equals(UpdateAdminEventDto.StateActionAdmin.REJECT_EVENT)
            && event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("An event can be rejected only if it has not been published yet");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ConflictException("An event can be published only if it is in the waiting state for publication");
        }

        updateEvent(dto, event);
        updateStatusByAdmin(dto, event);
        event.setPublishedOn(LocalDateTime.now());
        locationRepository.save(event.getLocation());
        Event saved = eventRepository.save(event);
        log.debug("Update event by admin. From Dto: {}", dto);
        return mapper.toEventFullDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> publicGetAllEvents(PublicEventsFilters filters, PageRequest page, HttpServletRequest request) {
        log.debug("Received request for public get all events. Filters: {}", filters);
        BooleanBuilder updateFilters = filters.getPredicate().and(QEvent.event.state.eq(State.PUBLISHED));
        List<Event> events = eventRepository.findAll(updateFilters, page.withSort(filters.getSort())).getContent();
        clientService.hit(new RequestStatDto(APP,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));
        getStatistics(events);
        log.debug("Found published events. Size: {}", events.size());
        return mapper.toShortDtoList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto publicGetEvent(long id, HttpServletRequest request) {
        log.debug("Received request for public get event. Event id: {}", id);
        BooleanExpression expression = QEvent.event.id.eq(id).and(QEvent.event.state.eq(State.PUBLISHED));
        Event event = eventRepository.findOne(expression)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        log.debug("Found published event with id : {}", id);
        ResponseEntity<String> hit = clientService.hit(new RequestStatDto(APP,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));
        log.debug("Hit status: {}", hit.getStatusCode());
        List<ResponseStatDto> stats = clientService
                .getStats(event.getPublishedOn(), LocalDateTime.now(), List.of(request.getRequestURI()), true);
        if (!stats.isEmpty()) {
            event.setViews(stats.getFirst().getHits());
        }
        return mapper.toEventFullDto(event);
    }

    private void updateEvent(BasicUpdateEventDto dto, Event event) {
        if (dto == null) {
            return;
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getCategory() != null) {
            event.setCategory(Category.builder().id(dto.getCategory()).build());
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
    }

    private void updateStatusByAdmin(UpdateAdminEventDto dto, Event event) {
        if (dto.getStateAction() != null) {
            if (dto.getStateAction().equals(UpdateAdminEventDto.StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
            } else {
                event.setState(State.CANCELED);
            }
        }
    }

    private void updateStatusByUser(UpdateUserEventDto dto, Event event) {
        if (dto.getStateAction() != null) {
            UpdateUserEventDto.StateActionUser stateAction = dto.getStateAction();
            if (stateAction.equals(UpdateUserEventDto.StateActionUser.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
    }

    private void getStatistics(List<Event> events) {
        String regExp = "/events/";
        List<String> uris = events.stream().map(Event::getId).map(id -> regExp + id).toList();
        LocalDateTime start = events.stream().map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo).orElse(LocalDateTime.now().minusDays(5));
        List<ResponseStatDto> stats = clientService.getStats(start, LocalDateTime.now(), uris, true);
        Map<Long, Event> eventsMap = events.stream().collect(Collectors.toMap(Event::getId, event -> event));
        for (ResponseStatDto stat : stats) {
            String uri = stat.getUri();
            String s = uri.replaceAll(regExp, "");
            eventsMap.get(Long.parseLong(s)).setViews(stat.getHits());
        }
    }
}
