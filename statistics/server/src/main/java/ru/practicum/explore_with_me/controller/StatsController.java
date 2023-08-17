package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.dto.HitDto;
import ru.practicum.explore_with_me.dto.HitStatDto;
import ru.practicum.explore_with_me.service.StatsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping()
    public ResponseEntity greet() {
        return ResponseEntity.ok("Hi");
    }

    @PostMapping("/hit")
    public ResponseEntity hit(HitDto hitDto) {
        statsService.hit(hitDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HitStatDto>> stats(
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris", defaultValue = "", required = false) List uris,
            @RequestParam(name = "unique", defaultValue = "false", required = false) boolean unique
    ) {
        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}