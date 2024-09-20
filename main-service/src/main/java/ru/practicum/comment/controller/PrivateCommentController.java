package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping("/{eventId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable long userId,
                                        @PathVariable long eventId,
                                        @Valid @RequestBody CommentShortDto commentDto) {
        return service.createComment(userId, eventId, commentDto);
    }

    @GetMapping
    public List<CommentFullDto> getAllCommentsByUserId(@PathVariable Long userId) {
        return service.getAllCommentByUserId(userId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateCommentById(@PathVariable Long userId,
                                            @PathVariable Long commentId,
                                            @RequestBody CommentShortDto commentDto) {
        return service.updateCommentById(userId, commentId, commentDto);
    }

    @GetMapping("/{commentId}")
    public CommentFullDto getCommentById(@PathVariable Long userId, @PathVariable Long commentId) {
        return service.getCommentById(userId, commentId);
    }
}
