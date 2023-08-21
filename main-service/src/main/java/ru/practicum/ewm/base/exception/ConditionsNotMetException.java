package ru.practicum.ewm.base.exception;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String message) {
        super(message);
    }

    public ConditionsNotMetException(String message, Throwable cause) {
        super(message, cause);
    }
}

