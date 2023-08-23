package ru.practicum.ewm.common.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.common.dto.comment.CommentCreateDto;
import ru.practicum.ewm.common.dto.comment.CommentFullDto;
import ru.practicum.ewm.common.entity.Comment;

@UtilityClass
public class CommentMapper {
    public static Comment dtoToObject(CommentCreateDto dto) {

        return Comment.builder()
                .text(dto.getText())
                .build();
    }

    public static CommentFullDto objectToFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .createdOn(comment.getCreatedOn())
                .updateOn(comment.getUpdateOn())
                .commentator(UserMapper.toUserShortDto(comment.getCommentator()))
                .text(comment.getText())
                .build();
    }
}