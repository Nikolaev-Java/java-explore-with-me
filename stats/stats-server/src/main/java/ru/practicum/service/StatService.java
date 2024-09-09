package ru.practicum.service;

import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void createStat(RequestStatDto dto);

    List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
