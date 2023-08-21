package ru.practicum.explore_with_me.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.dto.UpdateCompilationDto;
import ru.practicum.explore_with_me.service.CompilationService;
import ru.practicum.explore_with_me.service.UserEventService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final UserEventService userEventService;

    @PostMapping()
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление подборки {}", newCompilationDto.toString());
        return compilationService.create(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        log.info("Получен запрос PATCH /admin/compilations/{} на изменение подборки.", compId);
        return new ResponseEntity<>(compilationService.update(compId, updateCompilationDto), HttpStatus.OK);
    }

/*    @PatchMapping("/{compId}/pin")
    public CompilationDto pin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на закрепление на главной подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return compilationService.pinUnpin(compId, true);
    }*/

/*    @DeleteMapping("/{compId}/pin")
    public CompilationDto unpin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на открепление на главной подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return compilationService.pinUnpin(compId, false);
    }*/

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        log.info("{}: Запрос к эндпоинту '{}' на удаление подборки с ID={}", compId);
        compilationService.delete(compId);
    }

/*    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromComp(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на удаление события с ID={} из подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
        userEventFacade.deleteEventFromComp(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEventToComp(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление события с ID={} в подборку с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
        return userEventFacade.addEventToComp(compId, eventId);
    }*/
}