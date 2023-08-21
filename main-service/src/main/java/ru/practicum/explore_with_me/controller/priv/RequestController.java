package ru.practicum.explore_with_me.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.UserEventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class RequestController {

    private final UserEventService userEventService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserId(@PathVariable Long userId) {
        log.info("{}: Запрос к эндпоинту '{}' на получение заявок на участие от пользователя с ID={}", userId);
        return userEventService.getRequestsByUserId(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto create(@Valid @RequestParam Long eventId,
                                          @PathVariable Long userId) {
        log.info("{}: Запрос к эндпоинту '{}' на участие в событии с ID={} от пользователя с ID={}", eventId, userId);
        return userEventService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("{}: Запрос к эндпоинту '{}' на отмену заявки на участие с ID={} от пользователя с ID={}", requestId, userId);
        return userEventService.cancelRequest(userId, requestId);
    }
}