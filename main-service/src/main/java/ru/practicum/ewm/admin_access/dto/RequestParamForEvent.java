package ru.practicum.ewm.admin_access.dto;

import lombok.*;
import ru.practicum.ewm.common.enums.State;

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

    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    @NotNull
    private LocalDateTime rangeStart;
    @NotNull
    private LocalDateTime rangeEnd;
    @PositiveOrZero
    private int from;
    @Positive
    private int size;

    public boolean isValid() {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            return false;
        }
        return true;
    }
}