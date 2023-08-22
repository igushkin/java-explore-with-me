package ru.practicum.explore_with_me.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity handleBadRequest(final Exception e) {
        return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
    }
}