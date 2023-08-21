package ru.practicum.explore_with_me.exception;

public class NotValidException extends IllegalArgumentException {
    public NotValidException(String message) {
        super(message);
    }
}