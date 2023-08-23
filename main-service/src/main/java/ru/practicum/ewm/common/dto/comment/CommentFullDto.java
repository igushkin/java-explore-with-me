package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CommentFullDto {

    private final Long id;
    private final String createdOn;
    private final String updateOn;
    private final UserShortDto commentator;
    private final String text;
    private List<CommentFullDto> subComments;
}
