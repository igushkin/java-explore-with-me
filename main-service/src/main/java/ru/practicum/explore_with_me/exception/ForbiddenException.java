package ru.practicum.explore_with_me.exception;

import lombok.extern.slf4j.Slf4j;

public class ForbiddenException extends IllegalArgumentException {
    public ForbiddenException(String message) {
        super(message);
    }
}