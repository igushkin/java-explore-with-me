package ru.practicum.ewm.public_access.dto;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    @NotNull
    private LocalDateTime rangeStart;
    @NotNull
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    @PositiveOrZero
    private int from;
    @Positive
    private int size;
    private HttpServletRequest request;

    public boolean isValid() {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            return false;
        }
        return true;
    }
}