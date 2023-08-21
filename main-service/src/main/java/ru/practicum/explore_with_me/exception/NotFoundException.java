package ru.practicum.explore_with_me.exception;

import lombok.extern.slf4j.Slf4j;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
    }
}