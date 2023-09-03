package ru.practicum.ewm.private_access.service.event;

import ru.practicum.ewm.common.dto.comment.CommentCreateDto;
import ru.practicum.ewm.common.dto.comment.CommentFullDto;
import ru.practicum.ewm.common.dto.event.*;
import ru.practicum.ewm.common.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventsService {
    List<EventShortDto> getAll(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);


    CommentFullDto createComment(CommentCreateDto dto, Long userId, Long eventId);

    CommentFullDto updateComment(CommentCreateDto dto, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);
}