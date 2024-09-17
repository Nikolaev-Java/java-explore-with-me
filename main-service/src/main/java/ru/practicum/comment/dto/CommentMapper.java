package ru.practicum.comment.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.comment.Comment;
import ru.practicum.event.Event;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "user", source = "owner.id")
    CommentShortDto toCommentShortDto(Comment comment);

    @Mapping(target = "user", source = "owner.id")
    List<CommentShortDto> toCommentShortDtoList(List<Comment> comments);

    @Mapping(target = "owner", source = "user", qualifiedByName = "toUser")
    Comment fromCommentShortDto(CommentShortDto commentDto);

    CommentFullDto toCommentFullDto(Comment comment);

    List<CommentFullDto> toCommentFullDtoList(List<Comment> comments);

    @Mapping(target = "user", source = "owner.id")
    @Mapping(target = "comment", source = "commentByOwnerEvent")
    CommentEventOwnerDto toCommentEventOwnerDto(Comment comment);

    @Mapping(target = "user", source = "owner.id")
    @Mapping(target = "comment", source = "commentByOwnerEvent")
    List<CommentEventOwnerDto> toCommentEventOwnerDtoList(List<Comment> comments);

    @Named("toEvent")
    default Event fromEventIdToEvent(Long eventId) {
        return Event.builder().id(eventId).build();
    }

    @Named("toUser")
    default User fromUserIdToUser(Long id) {
        return User.builder().id(id).build();
    }

    default EventShortDto toEventShortDto(Event event) {
        return Mappers.getMapper(EventMapper.class).toShortDto(event);
    }
}
