package ru.practicum;

import org.springframework.stereotype.Component;
import ru.practicum.repository.StatView;

import java.util.List;

/**
 * A class for converting DTO objects into entity objects Stat
 */
@Component
public class MappingStat {
    /**
     * Converting a DTO from a query to an entity Stat
     *
     * @param dto - request stat dto
     * @return entity stat
     */
    public Stat requestStatDtoToStat(RequestStatDto dto) {
        if (dto == null) return null;
        return Stat.builder()
                .app(dto.getApp())
                .ip(dto.getIp())
                .uri(dto.getUri())
                .timestamp(dto.getTimestamp())
                .build();
    }

    /**
     * Converting the entity of the response to a database query into a response object
     *
     * @param stat - presentation of the database response
     * @return dto response
     */
    public ResponseStatDto statViewToResponseStatDto(StatView stat) {
        if (stat == null) return null;
        return ResponseStatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits((int) stat.getHits())
                .build();
    }

    /**
     * Converting the list entity's of the response to a database query into a list responses object
     *
     * @param stats - list presentations of the database response
     * @return list dto responses
     */
    public List<ResponseStatDto> statToResponseStatDto(List<StatView> stats) {
        return stats.stream().map(this::statViewToResponseStatDto).toList();
    }
}
