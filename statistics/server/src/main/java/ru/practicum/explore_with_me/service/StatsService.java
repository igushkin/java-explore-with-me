package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;
import ru.practicum.explore_with_me.entity.Hit;
import ru.practicum.explore_with_me.mapper.HitMapper;
import ru.practicum.explore_with_me.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final HitRepository hitRepository;

    public HitDto hit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        hitRepository.saveAndFlush(hit);
        return hitDto;
    }

    public List<HitStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        uris = uris.size() == 0 ? null : uris;

        if (unique)
            return hitRepository.getUniqieHitStat(start, end, uris);
        else
            return hitRepository.getHitStat(start, end, uris);
    }
}