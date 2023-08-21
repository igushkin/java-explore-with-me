package ru.practicum.explore_with_me.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.entity.Location;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminUpdateEventRequest {
    private String title;
    private String description;
    private String annotation;
    @JsonProperty("category")
    private Long categoryId;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
}