package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    HitDto hit(HitDto hitDto);

    List<HitStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}