package ru.practicum.ewm.public_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.comment.CommentFullDto;
import ru.practicum.ewm.public_access.service.event.PublicEventsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class PublicCommentController {

    private final PublicEventsService publicEventsService;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<CommentFullDto>> getCommentsByEventId(@PathVariable Long eventId,
                                                                     @RequestParam(defaultValue = "0") int from,
                                                                     @RequestParam(defaultValue = "10") int size) {

        log.info("Received a request to get comments by event id. eventId = {}", eventId);

        return new ResponseEntity<>(publicEventsService.getEventComments(eventId, from, size), HttpStatus.OK);
    }
}