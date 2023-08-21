package ru.practicum.explore_with_me.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EventFullDto;
import ru.practicum.explore_with_me.dto.EventShortDto;
import ru.practicum.explore_with_me.dto.NewEventDto;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.model.UpdateEventRequest;
import ru.practicum.explore_with_me.service.UserEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class EventController {

    private final UserEventService userEventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("{}: Запрос к эндпоинту '{}' от пользователя с ID={} " +
                "на получение {} добавленных событий, начиная с {}", userId, size, from);
        return userEventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto create(@Valid @RequestBody NewEventDto newEventDto,
                               @PathVariable Long userId) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление события {}", newEventDto.toString());
        return userEventService.createEvent(newEventDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("{}: Запрос к эндпоинту '{}' пользователем с ID={} на получения события с ID={}", userId, eventId);
        return userEventService.getUserEventById(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("{}: Запрос к эндпоинту '{}' на отмену события с ID={}", eventId);
        return userEventService.updateEvent(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("{}: Запрос к эндпоинту '{}' от пользователя с ID={} " +
                "на получение заявок на участие в событии с ID={} ", userId, eventId);
        return userEventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public void updateUserStatus(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("{}: Запрос к эндпоинту '{}' на подтверждение заявки с ID={} " +
                "на участие в событии с ID={} ", eventId);
        //return userEventService.up(userId, eventId);
    }
}