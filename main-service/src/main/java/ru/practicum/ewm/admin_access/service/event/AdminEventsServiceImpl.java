package ru.practicum.ewm.admin_access.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.admin_access.dto.RequestParamForEvent;
import ru.practicum.ewm.common.dto.event.EventFullDto;
import ru.practicum.ewm.common.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.enums.AdminStateAction;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.EventMapper;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.util.MyPageRequest;
import ru.practicum.ewm.common.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest dto) {
        if (dto.getEventDate() != null) {
            checkEventDate(dto.getEventDate());
        }
        Event eventUpdate = EventMapper.toEntity(dto);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", eventId)));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
        } else if (event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: CANCELED");
        }

        var stateAction = dto.getStateAction() == null ? null : dto.getStateAction().toString();

        if (AdminStateAction.PUBLISH_EVENT.toString().equals(stateAction)) {
            event.setState(State.PUBLISHED);
        } else if (AdminStateAction.REJECT_EVENT.toString().equals(stateAction)) {
            event.setState(State.CANCELED);
        }

        UtilMergeProperty.copyProperties(eventUpdate, event);

        try {
            eventRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Update event: {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(RequestParamForEvent param) {
        MyPageRequest pageable = new MyPageRequest(param.getFrom(), param.getSize(),
                Sort.by(Sort.Direction.ASC, "id"));

        List<Event> events = eventRepository.findEventsByParams(
                param.getUsers(),
                param.getStates(),
                param.getCategories(),
                param.getRangeStart(),
                param.getRangeEnd(),
                pageable);

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: the date and time for which the event is scheduled" +
                    " cannot be earlier than two hours from the current moment. Value: " + eventDate);
        }
    }
}