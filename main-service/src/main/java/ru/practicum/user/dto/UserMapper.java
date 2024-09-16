package ru.practicum.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.user.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User newUserRequestDtoToUser(NewUserRequestDto newUserRequestDto);

    UserDto userToUserDto(User user);

    List<UserDto> userListToUserDtoList(List<User> userList);

    UserShortDto userToUserShortDto(User user);
}
