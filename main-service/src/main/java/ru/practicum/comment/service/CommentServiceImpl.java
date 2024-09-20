package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.StatusComment;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentFullDto createComment(Long userId, Long eventId, CommentShortDto commentDto) {
        log.debug("Received request for private create comment. User id: {}. Event id: {}", userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("You are not allowed to create a new comment");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event state is not published");
        }
        Comment comment = commentMapper.fromCommentShortDto(commentDto);
        comment.setOwner(User.builder().id(userId).build());
        comment.setEvent(event);
        comment.setStatus(StatusComment.PENDING);
        Comment saved = commentRepository.save(comment);
        log.debug("Saved comment to db. Comment id: {}", saved.getId());
        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    public List<CommentFullDto> getAllCommentByUserId(Long userId) {
        log.debug("Received request for public get all comments. User id: {}", userId);
        List<Comment> comments = commentRepository.findByOwnerId(userId);
        log.debug("Received request for public get all comments. Size: {}", comments.size());
        return commentMapper.toCommentFullDtoList(comments);
    }

    @Override
    @Transactional
    public CommentFullDto updateCommentById(Long userId, Long commentId, CommentShortDto commentDto) {
        log.debug("Received request for public update comment. User id: {}. Comment id: {}", userId, commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getStatus().equals(StatusComment.PUBLISHED)) {
            throw new IllegalArgumentException("Comment status is published");
        }
        comment.setText(commentDto.getText());
        Comment saved = commentRepository.save(comment);
        log.debug("Update comment to db. Comment id: {}. Text", comment.getText());
        return commentMapper.toCommentFullDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentFullDto getCommentById(Long userId, Long commentId) {
        log.debug("Received request for public get comment. User id: {}. Comment id: {}", userId, commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return commentMapper.toCommentFullDto(comment);
    }

}
