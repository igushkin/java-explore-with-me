package ru.practicum.explore_with_me.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.entity.Hit;

@UtilityClass
public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getUri())
                .timestamp(hitDto.getTimestamp())
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getUri())
                .timestamp(hit.getTimestamp())
                .build();
    }
}