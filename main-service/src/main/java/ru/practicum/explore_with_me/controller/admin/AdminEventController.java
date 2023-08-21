package ru.practicum.explore_with_me.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EventFullDto;
import ru.practicum.explore_with_me.model.AdminUpdateEventRequest;
import ru.practicum.explore_with_me.model.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    @Value("${date.time.format}")
    private String dateTimeFormat;

/*    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        log.info("{}: Запрос к эндпоинту '{}' на публикацию события с ID={}", eventId);
        return eventService.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        log.info("{}: Запрос к эндпоинту '{}' на отклонение события с ID={}", eventId);
        return eventService.reject(eventId);
    }*/

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        //log.info("{}: Запрос к эндпоинту '{}' на редактирование события с ID={}", eventId);
        return userEventFacade.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) Set<Long> users,
                                        @RequestParam(required = false) Set<State> states,
                                        @RequestParam(required = false) Set<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("{}: Запрос к эндпоинту '{}' на получение списка событий", 1);

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart,
                DateTimeFormatter.ofPattern(dateTimeFormat)) : LocalDateTime.now();

        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd,
                DateTimeFormatter.ofPattern(dateTimeFormat)) : LocalDateTime.now().plusYears(300);

        return eventService.getEventsForAdmin(users, states, categories, start, end, from, size);
    }
}