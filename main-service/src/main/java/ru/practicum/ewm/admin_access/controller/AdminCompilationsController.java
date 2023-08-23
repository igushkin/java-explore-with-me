package ru.practicum.ewm.admin_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin_access.service.compilation.AdminCompilationService;
import ru.practicum.ewm.common.dto.compilation.CompilationDto;
import ru.practicum.ewm.common.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.common.dto.compilation.UpdateCompilationRequest;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос POST /admin/compilations c новой подборкой: {}", newCompilationDto.getTitle());
        return new ResponseEntity<>(adminCompilationService.save(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{}", compId);
        adminCompilationService.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос PATCH /admin/compilations/{} на изменение подборки.", compId);
        return new ResponseEntity<>(adminCompilationService.update(compId, updateCompilationRequest), HttpStatus.OK);
    }
}