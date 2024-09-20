package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;

import java.util.List;

public interface CommentService {
    CommentFullDto createComment(Long userId, Long eventId, CommentShortDto commentDto);

    List<CommentFullDto> getAllCommentByUserId(Long userId);

    CommentFullDto updateCommentById(Long userId, Long commentId, CommentShortDto commentDto);

    CommentFullDto getCommentById(Long userId, Long commentId);
}
