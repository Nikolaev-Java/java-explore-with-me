package ru.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The REST controller of the statistics service. Adding data, getting statistics
 */
@RestController
@RequiredArgsConstructor
public class StatServerController {
    private final StatService service;

    @PostMapping("/hit")
    public void hit(@Valid @RequestBody @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") RequestStatDto requestStatDto) {
        service.createStat(requestStatDto);
    }

    @GetMapping("/stats")
    public List<ResponseStatDto> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
