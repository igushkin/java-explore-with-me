package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SERVICE_URI = "http://localhost:9090/";

    private final RestTemplate restTemplate;

    public ResponseEntity<String> hit(HitDto hitDto) {
        return restTemplate.postForEntity(SERVICE_URI + "/hit", hitDto, String.class);
    }

    public List<HitStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        var strBuilder = new StringBuilder();
        var startStr = start.format(DATE_TIME_FORMATTER);
        var endStr = end.format(DATE_TIME_FORMATTER);

        for (var uri : uris) {
            strBuilder.append("&uris=" + uri);
        }

        var urisStr = strBuilder.toString();

        String url = SERVICE_URI + "stats/" + "?start=" + startStr + "&end=" + endStr + urisStr + "&unique=" + unique;

        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<HitStatDto>>() {
        }).getBody();
    }
}