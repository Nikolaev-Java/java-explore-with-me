package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query("""
            SELECT s.app app, s.uri uri, COUNT(DISTINCT s.ip) hits FROM Stat s
            WHERE ?1 is null OR s.uri in ?1 and s.timestamp BETWEEN ?2 and ?3
            GROUP BY s.app, s.uri
            ORDER BY hits DESC
            """)
    List<StatView> findAllStatsByUriUniqIp(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT s.app app, s.uri uri, count(*) hits FROM Stat s
            WHERE ?1 is null OR s.uri in ?1 and s.timestamp BETWEEN ?2 and ?3
            GROUP BY s.app, s.uri
            ORDER BY hits DESC
            """)
    List<StatView> findAllStatsByUri(List<String> uris, LocalDateTime start, LocalDateTime end);
}
