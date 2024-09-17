package ru.practicum.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph("user_event")
    List<Comment> findByOwnerId(long id);

    @EntityGraph("user_event")
    Optional<Comment> findByIdAndEventId(long id, long eventId);

    @EntityGraph("user_event")
    List<Comment> findAllByEventId(long eventId, Pageable pageable);
}
