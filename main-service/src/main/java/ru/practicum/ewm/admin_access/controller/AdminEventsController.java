package ru.practicum.ewm.admin_access.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin_access.dto.RequestParamForEvent;
import ru.practicum.ewm.admin_access.service.event.AdminEventsService;
import ru.practicum.ewm.common.dto.event.EventFullDto;
import ru.practicum.ewm.common.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
@Validated
public class AdminEventsController {

    public final AdminEventsService adminEventsService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAll(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /admin/events");

        List<State> statesEnum = null;
        if (states != null) {
            statesEnum = states.stream().map(State::from).filter(Objects::nonNull).collect(Collectors.toList());
        }

        RequestParamForEvent param = RequestParamForEvent.builder()
                .users(users)
                .states(statesEnum)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        if (!param.isValid()) {
            throw new BadRequestException("Request params are not valid");
        }

        return new ResponseEntity<>(adminEventsService.getAll(param), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long eventId,
                                               @Valid @RequestBody UpdateEventAdminRequest updateEvent) {
        log.info("Получен запрос PATCH /admin/events/{} на изменение события.", eventId);
        return new ResponseEntity<>(adminEventsService.update(eventId, updateEvent), HttpStatus.OK);
    }
}