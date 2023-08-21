package ru.practicum.explore_with_me.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping()
    public Collection<CompilationDto> getCompilations(@Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                      @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                                      @Valid @RequestParam(required = false) Boolean pinned) {
        log.info("{}: Запрос к эндпоинту '{}' на получение подборок начиная с {} в количестве {}", from, size);
        return compilationService.getCompilations(from, size, pinned);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("{}: Запрос к эндпоинту '{}' на получение подборки с ID={}", compId);
        return compilationService.getCompilationById(compId);
    }
}