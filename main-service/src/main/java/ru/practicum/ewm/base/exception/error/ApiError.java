package ru.practicum.ewm.base.exception.error;

import lombok.Getter;
import ru.practicum.ewm.base.mapper.DateTimeMapper;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = DateTimeMapper.toStringDate(LocalDateTime.now());
    }
}
