package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.entity.*;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.model.UpdateEventRequest;
import ru.practicum.explore_with_me.exception.*;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.mapper.UserMapper;

import java.util.List;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventService {
    private final UserService userService;
    private final EventService eventService;
    private final CategoryService categoryService;
    private UserMapper userMapper;
    private final EventMapper eventMapper;
    private CategoryMapper categoryMapper;
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;
    private final RequestService requestService;

    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        UserDto initiator = userService.getUserById(userId);
        CategoryDto categoryDto = categoryService.getCategoryById(newEventDto.getCategoryId());
/*        NewEventDto eventCreateDto = eventMapper.newEventDtoToEventCreateDto(newEventDto,
                initiator, categoryDto);*/
        return eventService.create(newEventDto, categoryDto, initiator);
    }

    public EventFullDto cancelEvent(Long eventId, Long userId) {
        userService.getUserById(userId);
        return eventService.cancel(eventId, userId);
    }

    public EventFullDto getUserEventById(Long eventId, Long userId) {
        userService.getUserById(userId);
        return eventService.getEventByIdForUser(eventId, userId);
    }

    public void deleteEventFromComp(Long compId, Long eventId) {
        Event event = eventService.getEvent(eventId);
        compilationService.deleteEvent(compId, event);
    }

    public CompilationDto addEventToComp(Long compId, Long eventId) {
        Event event = eventService.getEvent(eventId);
        return compilationService.addEvent(compId, event);
    }

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        //log.info("Получен пользователь {}", user);
        Event event = eventService.getEvent(eventId);
        //log.info("Получено событие {}", event);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии!");
        }
        if (requestService.getRequestByUserIdAndEventId(userId, eventId) != null) {
            throw new ForbiddenException("Нельзя добавить повторный запрос!");
        }

        return requestService.createRequest(user, event);
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userService.getUser(userId);
        log.info("Получен пользователь {}", user);
        return requestService.cancelRequest(user, requestId);
    }

    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        User user = userService.getUser(userId);
        log.info("Получен пользователь {}", user);
        Event event = eventService.getEvent(eventId);
        log.info("Получено событие {}", event);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Запрос на участие в событии может подтверждать только инициатор!");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя подтверждать запрос на участие в неопубликованном событии!");
        }

        return requestService.confirmRequest(event, requestId);
    }

    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        User user = userService.getUser(userId);
        log.info("Получен пользователь {}", user);
        Event event = eventService.getEvent(eventId);
        log.info("Получено событие {}", event);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Запрос на участие в событии может отклонять только инициатор!");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя отклонять запрос на участие в неопубликованном событии!");
        }

        return requestService.rejectRequest(event, requestId);
    }

    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        User user = userService.getUser(userId);
        log.info("Получен пользователь {}", user);
        return requestService.getRequestsByUserId(userId);
    }

    public List<ParticipationRequestDto> getRequestsByEventId(Long userId, Long eventId) {
        var user = userService.getUser(userId);
        var event = eventService.getEventByIdForUser(userId, eventId);

        //log.info("Получен пользователь {}", user);
        //log.info("Получено событие {}", user);

        return requestService.getRequestsByEventId(eventId);
    }

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        User user = userService.getUser(userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Дата и время события не могут быть раньше," +
                    " чем через два часа от текущего момента");
        }

        User user = userService.getUser(userId);
/*        Category category = null;

        if (updateEventRequest.getCategoryId() != null) {
            category = categoryService.getCategoryById(updateEventRequest.getCategoryId());
        }*/

        EventFullDto eventFullDto = eventService.updateEvent(userId, eventId, updateEventRequest);
        return eventFullDto;
    }

    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {

        Category category = null;
        if (adminUpdateEventRequest.getCategoryId() != null) {
            category = categoryService.getCategory(adminUpdateEventRequest.getCategoryId());
        }

        EventFullDto eventFullDto = eventService.updateEventByAdmin(eventId, adminUpdateEventRequest, category);
        return eventFullDto;
    }

    public void deleteCategory(Long categoryId) {

        List<EventShortDto> events = eventService.getEventsByCategoryId(categoryId);

        if (events.size() != 0) {
            throw new NotValidException("Нельзя удалить категорию с событиями!");
        }
        categoryService.delete(categoryId);
    }
}