package ru.practicum.explore_with_me.dto;

import lombok.*;
import ru.practicum.explore_with_me.entity.Location;
import ru.practicum.explore_with_me.model.State;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class EventFullDto  {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
    private Integer participantLimit;
    private LocalDateTime createdOn;
    private String description;
    private Location location;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    //private Long id;
    //private String annotation;
    //private CategoryDto category;
    //private Long confirmedRequests;
    //private String title;
    //private Long views;
    //private LocalDateTime eventDate;
    //private UserShortDto initiator;
    //private boolean paid;

}

/*
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
 */