package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.MappingStat;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.Stat;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralStatService implements StatService {
    private final StatRepository statRepository;
    private final MappingStat mappingStat;

    @Override
    public void createStat(RequestStatDto dto) {
        log.debug("Received stat dto: {}", dto.toString());
        Stat saved = statRepository.save(mappingStat.requestStatDtoToStat(dto));
        log.debug("Stat created this id: {}", saved.getId());
    }

    @Override
    public List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.debug("Received request for statistics between {} and: {}. Unique ip: {}. For uris : {}",
                start, end, unique, uris);
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must not be before start time.");
        }
        List<ResponseStatDto> response;
        if (unique) {
            response = mappingStat.statToResponseStatDto(statRepository.findAllStatsByUriUniqIp(uris, start, end));
        } else {
            response = mappingStat.statToResponseStatDto(statRepository.findAllStatsByUri(uris, start, end));
        }
        log.debug("Response: {}", response);
        return response;
    }
}
