package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explore_with_me.model.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    @JsonProperty("event")
    @NotNull
    private Long eventId;
    @JsonProperty("requester")
    @NotNull
    private Long requesterId;
    private Status status;
}