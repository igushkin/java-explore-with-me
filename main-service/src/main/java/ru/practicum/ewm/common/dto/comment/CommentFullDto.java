package ru.practicum.ewm.common.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.common.dto.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CommentFullDto {

    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime updateOn;
    private UserShortDto commentator;
    private String text;
}