package ru.practicum.ewm.publicApi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.EventShortDto;
import ru.practicum.ewm.base.exception.BadRequestException;
import ru.practicum.ewm.publicApi.dto.RequestParamForEvent;
import ru.practicum.ewm.publicApi.service.event.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventsController {

    public final PublicEventsService eventsService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAll(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size,
                                                      HttpServletRequest request) {
        log.info("Получен запрос GET /events c параметрами: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        RequestParamForEvent param = RequestParamForEvent.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .request(request)
                .build();

        if (!param.isValid()) {
            throw new BadRequestException("");
        }

        return new ResponseEntity<>(eventsService.getAll(param), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> get(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", id);
        return new ResponseEntity<>(eventsService.get(id, request), HttpStatus.OK);
    }
}