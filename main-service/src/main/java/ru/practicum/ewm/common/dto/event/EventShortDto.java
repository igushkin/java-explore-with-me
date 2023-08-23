package ru.practicum.ewm.common.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.common.dto.category.CategoryDto;
import ru.practicum.ewm.common.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private long views;

}