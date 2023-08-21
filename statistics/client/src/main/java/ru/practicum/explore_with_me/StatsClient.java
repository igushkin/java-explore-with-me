package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;

import java.net.URLEncoder;
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

        strBuilder.append(start.format(DATE_TIME_FORMATTER));
        strBuilder.append(end.format(DATE_TIME_FORMATTER));

        for (var uri : uris) {
            strBuilder.append("&uris=" + uri);
        }

        strBuilder.append("&unique=" + unique);

        String url = URLEncoder.encode(strBuilder.toString());
        return (List<HitStatDto>) (restTemplate.getForEntity(url, List.class));
    }
}