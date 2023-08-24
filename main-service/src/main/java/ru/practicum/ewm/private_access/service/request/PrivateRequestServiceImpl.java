package ru.practicum.ewm.private_access.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.entity.Request;
import ru.practicum.ewm.common.entity.User;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.enums.Status;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.RequestMapper;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {


    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        if (userRepository.existsById(userId)) {
            return RequestMapper.toDtoList(requestRepository.findAllByRequesterId(userId));
        } else {
            throw new NotFoundException(String.format("User not found with id = %s", userId));
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", eventId)));
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id = %s", userId)));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(String.format("Request with requesterId=%d and eventId=%d already exist", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User with id=%d must not be equal to initiator", userId));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event with id=%d is not published", eventId));
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }
        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(RequestMapper.toRequest(event, user)));
    }

    @Transactional
    @Override
    public ParticipationRequestDto update(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d " +
                        "and requesterId=%d was not found", requestId, userId)));
        request.setStatus(Status.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}