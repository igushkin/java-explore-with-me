package ru.practicum.ewm.publicApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.Compilation.CompilationDto;
import ru.practicum.ewm.publicApi.service.compilation.PublicCompilationsService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class PublicCompilationsController {

    public final PublicCompilationsService compilationsService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(required = false) Boolean pinned,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /compilations c параметрами: pinned = {}, from = {}, size = {}", pinned, from, size);
        return new ResponseEntity<>(compilationsService.getAll(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<CompilationDto> get(@PathVariable Long comId) {
        log.info("Получен запрос GET /compilations/{}", comId);
        return new ResponseEntity<>(compilationsService.get(comId), HttpStatus.OK);
    }
}
