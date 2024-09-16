package ru.practicum.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @EntityGraph("complication_events-graph")
    List<Compilation> findByPinned(boolean pinned, Pageable pageable);

    @EntityGraph("complication_events-graph")
    Page<Compilation> findAll(Pageable pageable);
}
