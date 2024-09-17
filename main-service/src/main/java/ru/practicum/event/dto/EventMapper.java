package ru.practicum.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.comment.Comment;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.Event;
import ru.practicum.user.User;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    EventShortDto toShortDto(Event event);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    Set<EventShortDto> toShortDtoSet(Set<Event> events);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    List<EventShortDto> toShortDtoList(List<Event> events);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    @Mapping(target = "category", source = "category", qualifiedByName = "idToCategory")
    Event fromNewEventDto(NewEventDto newEventDto);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "publishedOn")
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "createdOn")
    EventFullDto toEventFullDto(Event event);

    List<EventFullDto> toEventFullDtoList(List<Event> events);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss", source = "eventDate")
    EventCommentsShortDto toEventCommentsShortDto(Event event);

    default UserShortDto userToShortDto(User user) {
        return Mappers.getMapper(UserMapper.class).userToUserShortDto(user);
    }

    default CategoryDto categoryToCategoryDto(Category category) {
        return Mappers.getMapper(CategoryMapper.class).toCategoryDto(category);
    }

    default CommentShortDto toCommentShortDto(Comment comment) {
        return Mappers.getMapper(CommentMapper.class).toCommentShortDto(comment);
    }

    @Named(value = "idToCategory")
    default Category idToCategory(long category) {
        return Category.builder().id(category).build();
    }
}
