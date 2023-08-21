package ru.practicum.explore_with_me.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.explore_with_me.entity.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    @JsonProperty("category")
    private Long categoryId;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid = false;
    private Integer participantLimit = 10;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120)
    private String title;
}