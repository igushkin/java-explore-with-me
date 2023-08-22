package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

        //strBuilder.append(SERVICE_URI);
        //strBuilder.append("?start=" + start.format(DATE_TIME_FORMATTER));
        //strBuilder.append("&end=" + end.format(DATE_TIME_FORMATTER));

        for (var uri : uris) {
            strBuilder.append("&uris=" + uri);
        }

        var urisStr = strBuilder.toString();

        //strBuilder.append("&unique=" + unique);

        //String url = URLEncoder.encode(strBuilder.toString());

        String url = SERVICE_URI + "stats/" + "?start=" + startStr + "&end=" + endStr + urisStr + "&unique=" + unique;
        //url = url.replace("+", "%20");

/*        var a = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                },
                Map.of()
        );*/

        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<HitStatDto>>() {
        }).getBody();

        //return (List<HitStatDto>) ((restTemplate.getForEntity(url, List.class)).getBody());
    }
}