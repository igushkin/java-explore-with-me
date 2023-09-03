package ru.practicum.ewm.private_access.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.comment.CommentCreateDto;
import ru.practicum.ewm.common.dto.comment.CommentFullDto;
import ru.practicum.ewm.common.dto.event.*;
import ru.practicum.ewm.common.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.common.entity.Comment;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.entity.Request;
import ru.practicum.ewm.common.entity.User;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.enums.UserStateAction;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.CommentMapper;
import ru.practicum.ewm.common.mapper.EventMapper;
import ru.practicum.ewm.common.mapper.RequestMapper;
import ru.practicum.ewm.common.repository.*;
import ru.practicum.ewm.common.util.MyPageRequest;
import ru.practicum.ewm.common.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.enums.Status.CONFIRMED;
import static ru.practicum.ewm.common.enums.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoriesRepository categoriesRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        MyPageRequest pageRequest = new MyPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository.findAll(pageRequest).toList());
        log.info("Get events list size: {}", eventShorts.size());
        return eventShorts;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        log.info("Get event: {}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("Event not found with id = %s and userId = %s", eventId, userId));
        }
        return RequestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        checkEventDate(eventDto.getEventDate());
        Event event = EventMapper.toEntity(eventDto);
        event.setCategory(categoriesRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                        eventDto.getCategory()))));
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId))));
        event.setViews(0L);
        try {
            event = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add event: {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        Event eventTarget = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Event eventUpdate = EventMapper.toEntity(eventDto);
        checkEventDate(eventUpdate.getDate());

        if (eventDto.getCategory() != null) {
            eventUpdate.setCategory(categoriesRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                            eventDto.getCategory()))));
        }

        if (eventTarget.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event must not be published");
        }

        UtilMergeProperty.copyProperties(eventUpdate, eventTarget);

        if (eventDto.getStateAction() != null) {
            if (UserStateAction.CANCEL_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
                eventTarget.setState(State.CANCELED);
            } else if (UserStateAction.SEND_TO_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
                eventTarget.setState(State.PENDING);
            }
        }

        log.info("Update event: {}", eventTarget.getTitle());
        return EventMapper.toEventFullDto(eventTarget);
    }


    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        String status = request.getStatus();

        if (status.equals(REJECTED.toString())) {
            boolean isConfirmedRequestExists = requests.stream()
                    .anyMatch(r -> r.getStatus().equals(CONFIRMED));
            if (isConfirmedRequestExists) {
                throw new ConflictException("Cannot reject confirmed requests");
            }
            rejectedRequests = requests.stream()
                    .peek(r -> r.setStatus(REJECTED))
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        long availableParticipants = participantLimit - approvedRequests;
        long potentialParticipants = requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }

        if (status.equals(CONFIRMED.toString())) {
            if (participantLimit.equals(0L) || (potentialParticipants <= availableParticipants && !event.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(REJECTED)) {
                                r.setStatus(REJECTED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been rejected", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(Math.min(participantLimit, requests.size()));
            }
        }
        eventRepository.flush();
        requestRepository.flush();
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }


    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: the date and time for which the event is scheduled" +
                    " cannot be earlier than two hours from the current moment. Value: " + eventDate);
        }
    }

    @Transactional
    @Override
    public CommentFullDto createComment(CommentCreateDto dto, Long userId, Long eventId) {

        User commentator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User not found with id = %s", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event not found with id = %s", eventId)));

        if (!event.getState().equals(State.PUBLISHED)) {
            log.info("event state {}", event.getState());
            throw new BadRequestException("You can only comment on published events");
        }

        Comment comment = CommentMapper.dtoToObject(dto);

        comment.setCreatedOn(LocalDateTime.now());
        comment.setCommentator(commentator);
        comment.setEvent(event);

        Comment newComment = commentRepository.save(comment);

        return CommentMapper.objectToFullDto(newComment);
    }

    @Transactional
    @Override
    public CommentFullDto updateComment(CommentCreateDto dto, Long commentId, Long userId) {

        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Comment not found with id = %s", commentId)));

        comment.setUpdateOn(LocalDateTime.now());
        comment.setText(dto.getText());

        return CommentMapper.objectToFullDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId) {
        var affected = commentRepository.deleteByIdAndAndCommentatorId(commentId, userId);

        if (affected == 0) {
            throw new NotFoundException(String.format("Comment not found with id = %s", commentId));
        }
    }
}