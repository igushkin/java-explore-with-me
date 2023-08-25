package ru.practicum.ewm.common.dto.comment;

import lombok.*;
import ru.practicum.ewm.common.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentFullDto {

    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime updateOn;
    private UserShortDto commentator;
    private String text;
}