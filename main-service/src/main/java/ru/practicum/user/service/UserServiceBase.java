package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.DuplicatedDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.Participation;
import ru.practicum.participation.ParticipationRepository;
import ru.practicum.participation.Status;
import ru.practicum.participation.dto.ParticipationMapper;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceBase implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;
    private final EventRepository eventRepository;

    @Override
    public UserDto createUser(NewUserRequestDto requestDto) {
        log.debug("Received user dto: {}", requestDto.toString());
        try {
            User saved = repository.save(mapper.newUserRequestDtoToUser(requestDto));
            log.debug("User created with id {}", saved.getId());
            return mapper.userToUserDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedDataException("Email address already exists");
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Received id user for delete: {}", id);
        repository.deleteById(id);
        log.debug("User deleted with id {}", id);
    }

    @Override
    public List<ParticipationRequestDto> getAllParticipations(long userId) {
        log.debug("Received request all participations for user: {}", userId);
        if (!repository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        List<Participation> participations = participationRepository.findAllByUserId(userId);
        log.debug("All participations found for user {}", userId);
        return participationMapper.toParticipationRequestDtoList(participations);
    }

    @Override
    public ParticipationRequestDto createParticipation(long userId, long eventId) {
        log.debug("Received request create participation for user: {}, event: {}", userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with id " + eventId + " does not exist"));
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("You cannot send a request to participate in your event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You cannot send a request to participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() == event.getConfirmedRequests()) {
                throw new ConflictException("The limit of participants has been reached");
            }
        }
        Participation participation = Participation.builder()
                .status(Status.PENDING)
                .user(user)
                .event(event)
                .build();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        try {
            Participation saved = participationRepository.save(participation);
            eventRepository.save(event);
            log.debug("Participation created with id {}, status: {}", saved.getId(), saved.getStatus());
            return participationMapper.toParticipationRequestDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedDataException("You have already submitted a request to participate");
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipation(long userId, long requestId) {
        log.debug("Received request cancel participation for user: {}, request: {}", userId, requestId);
        Participation participation = participationRepository.findByIdAndUserId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Participation not found"));
        Integer deleted = participationRepository.deleteByIdAndUserId(requestId, userId);
        if (deleted < 1) {
            throw new NotFoundException("Participation not found");
        }
        participation.setStatus(Status.CANCELED);
        log.debug("Participation deleted with id {}", participation.getId());
        return participationMapper.toParticipationRequestDto(participation);
    }

    @Override
    public List<UserDto> getAllUsersById(List<Long> ids, Pageable pageable) {
        log.debug("Received list of users by id: {}, and page from {} to {}",
                ids, pageable.getPageNumber(), pageable.getPageSize());
        List<User> allById = repository.findAllByIdIn(ids, pageable);
        log.debug("Users found with ids {}", allById);
        return mapper.userListToUserDtoList(allById);
    }
}
