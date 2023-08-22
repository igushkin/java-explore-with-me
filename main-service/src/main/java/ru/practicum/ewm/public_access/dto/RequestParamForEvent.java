package ru.practicum.ewm.public_access.dto;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamForEvent {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    private int from;
    private int size;
    private HttpServletRequest request;

    public boolean isValid() {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            return false;
        }
        return true;
    }
}