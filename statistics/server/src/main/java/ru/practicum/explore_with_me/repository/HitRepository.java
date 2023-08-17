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



    @Query(value = "SELECT h FROM Hit h " +
            "where h.timestamp >= ?1 and h.timestamp <= ?2 ")
    List<HitStatDto> getHitStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}