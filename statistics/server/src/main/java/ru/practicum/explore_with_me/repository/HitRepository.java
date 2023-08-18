package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.dto.HitStatDto;
import ru.practicum.explore_with_me.entity.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query(value = "SELECT new ru.practicum.explore_with_me.dto.HitStatDto(h.app, h.uri, count(distinct h.ip)) " +
            "FROM Hit h " +
            "where h.timestamp >= ?1 and h.timestamp <= ?2 and (h.uri in ?3 or ?3 is null ) " +
            "group by h.uri, h.app " +
            "order by count(distinct h.ip) desc ")
    List<HitStatDto> getUniqueHitStat(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.explore_with_me.dto.HitStatDto(h.app, h.uri, count(h.id)) " +
            "FROM Hit h " +
            "where h.timestamp >= ?1 and h.timestamp <= ?2 and (h.uri in ?3 or ?3 is null ) " +
            "group by h.uri, h.app " +
            "order by count(h.ip) desc ")
    List<HitStatDto> getHitStat(LocalDateTime start, LocalDateTime end, List<String> uris);
}