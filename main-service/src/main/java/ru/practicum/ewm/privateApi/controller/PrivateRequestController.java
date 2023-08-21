package ru.practicum.ewm.privateApi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.privateApi.service.request.PrivateRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final PrivateRequestService service;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        return new ResponseEntity<>(service.getRequests(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId,
                                                          @RequestParam Long eventId) {
        log.info("Получен запрос POST /users/{}/requests c новым запросом на участие в Event с id = {}", userId, eventId);
        return new ResponseEntity<>(service.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestsId}/cancel")
    public ResponseEntity<ParticipationRequestDto> update(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("Получен запрос PATCH /users/{}/requests/{requestsId}/cancel" +
                " c отменой запроса id = {}", userId, requestsId);
        return new ResponseEntity<>(service.update(userId, requestsId), HttpStatus.OK);
    }


}
