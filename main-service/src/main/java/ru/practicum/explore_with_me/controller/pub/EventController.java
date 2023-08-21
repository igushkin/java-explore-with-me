package ru.practicum.explore_with_me.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EventFullDto;
import ru.practicum.explore_with_me.dto.EventShortDto;
import ru.practicum.explore_with_me.model.EventSort;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.service.StatsService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final StatsService statsService;
    @Value("${date.time.format}")
    private String dateTimeFormat;

    @GetMapping()
    public List<EventShortDto> getEvents(@RequestParam(required = false, defaultValue = "") String text,
                                         @RequestParam(required = false) Set<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) EventSort sort,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {

        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart,
                DateTimeFormatter.ofPattern(dateTimeFormat)) : LocalDateTime.now();

        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd,
                DateTimeFormatter.ofPattern(dateTimeFormat)) : LocalDateTime.now().plusYears(300);

        return eventService.getEventsForPublic(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId,  HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.getEventByIdForPublic(eventId);
        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());
        return eventFullDto;
    }
}