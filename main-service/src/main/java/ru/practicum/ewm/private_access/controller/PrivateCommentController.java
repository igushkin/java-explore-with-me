package ru.practicum.ewm.private_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.comment.CommentCreateDto;
import ru.practicum.ewm.common.dto.comment.CommentFullDto;
import ru.practicum.ewm.private_access.service.event.PrivateEventsService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/users/{userId}")
public class PrivateCommentController {

    private final PrivateEventsService privateEventsService;

    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentFullDto> createComment(@Valid @RequestBody CommentCreateDto dto,
                                                        @PathVariable Long userId,
                                                        @PathVariable Long eventId) {

        log.info("Received a request to create a comment. dto = {}, userId = {}, eventId = {}", dto, userId, eventId);

        return new ResponseEntity<>(privateEventsService.createComment(dto, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommentFullDto> updateComment(@Valid @RequestBody CommentCreateDto dto,
                                                        @PathVariable Long userId, @PathVariable Long commentId) {

        log.info("Received a request to update a comment. dto = {}, commentId = {}, userId = {}", dto, commentId, userId);

        return new ResponseEntity<>(privateEventsService.updateComment(dto, commentId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @PathVariable Long userId) {

        log.info("Received a request to delete a comment with an id " + commentId);

        privateEventsService.deleteComment(commentId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}