package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequestDto requestDto);

    void deleteUserById(Long id);

    List<UserDto> getAllUsersById(List<Long> ids, Pageable pageable);

    List<ParticipationRequestDto> getAllParticipations(long userId);

    ParticipationRequestDto createParticipation(long userId, long eventId);

    ParticipationRequestDto cancelParticipationById(long userId, long requestId);
}
