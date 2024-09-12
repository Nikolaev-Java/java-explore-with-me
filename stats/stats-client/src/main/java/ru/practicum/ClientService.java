package ru.practicum;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientService {
    ResponseEntity<String> hit(RequestStatDto requestStatDto);

    List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean uniqueIp);
}
