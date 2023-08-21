package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsService {

    private static final String APP_NAME = "ewm-service";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsClient statsClient;


    public Long getViews(String uri) {
        LocalDateTime start = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime end = LocalDateTime.now().plusYears(1000);

        List<HitStatDto> listStats = statsClient.getStats(start, end, List.of(uri), false);

        if (listStats != null && listStats.size() > 0) {
            listStats = listStats.stream()
                    .filter(x -> APP_NAME.equals(x.getApp()))
                    .collect(Collectors.toList());
            return listStats.size() > 0 ? listStats.get(0).getHits() : 0L;
        } else {
            return 0L;
        }
    }

    public void setHits(String uri, String ip) {

        HitDto hitDto = new HitDto(APP_NAME, uri, ip, LocalDateTime.now());

        //log.info("Отправляем запрос на сервер статистики для {}", uri);
        var response = statsClient.hit(hitDto);
        //log.info("Получен ответ от сервера статистики {}", endpointHitDto);
    }

}