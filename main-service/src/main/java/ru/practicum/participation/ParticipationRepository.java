package ru.practicum.participation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    @EntityGraph("participation_user_event-graph")
    List<Participation> findAllByUserId(Long userId);

    Optional<Participation> findByIdAndUserId(Long requestId, Long userId);

    Integer deleteByIdAndUserId(Long requestId, Long userId);

    @EntityGraph("participation_user_event-graph")
    Optional<Participation> findByUserIdAndEventId(Long userId, Long eventId);

    @EntityGraph("participation_user_event-graph")
    List<Participation> findAllByUserIdAndEventId(Long userId, Long eventId);

    @EntityGraph("participation_user_event-graph")
    List<Participation> findAllByEventId(Long eventId);

    List<Participation> findByIdIn(List<Long> ids);
}
