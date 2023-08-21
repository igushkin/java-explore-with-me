package ru.practicum.explore_with_me.exception;

public class AlreadyExistsException extends IllegalArgumentException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}