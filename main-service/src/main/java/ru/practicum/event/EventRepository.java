package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @EntityGraph("category_user")
    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long id, Long userId);
}
