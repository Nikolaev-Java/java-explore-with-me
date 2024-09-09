package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaseClientService implements ClientService {
    private final RestClient restClient;

    @Override
    public ResponseEntity<String> hit(RequestStatDto requestStatDto) {
        return restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestStatDto)
                .exchange(((clientRequest, clientResponse) -> {
                    if (clientResponse.getStatusCode().value() != 200) {
                        return ResponseEntity.badRequest().body(new BufferedReader(
                                new InputStreamReader(clientResponse.getBody())).lines().collect(Collectors.joining()));
                    } else {
                        return ResponseEntity.ok().build();
                    }
                }));
    }

    @Override
    public List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean uniqueIp) {
        Map<String, String> params = Map.of("start", formattingDate(start),
                "end", formattingDate(end),
                "uris", formattingListUris(uris),
                "uniqueIp", String.valueOf(uniqueIp));
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path("/stats")
                .query("start={start}")
                .query("end={end}")
                .query("uris={uris}")
                .query("unique={uniqueIp}")
                .buildAndExpand(params);
        return restClient.get()
                .uri(uriComponents.toString())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ResponseStatDto>>() {
                });
    }

    private String formattingListUris(List<String> uris) {
        return String.join(",", uris);
    }

    private String formattingDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
