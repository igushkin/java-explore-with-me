package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.entity.Event;
import ru.practicum.explore_with_me.exception.ForbiddenException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.exception.NotValidException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.EventSort;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.model.UpdateEventRequest;
import ru.practicum.explore_with_me.repository.EventRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.util.UtilityClass.getPage;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final EntityManager entityManager;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public EventFullDto create(NewEventDto newEventDto, CategoryDto categoryDto, UserDto userDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new NotValidException("Дата и время события не могут быть раньше, чем через два" +
                    " часа от текущего момента");
        }
        Event event = repository.save(mapper.newEventDtoToEvent(newEventDto, categoryDto, userDto));
        return mapper.eventToEventFullDto(event);
    }

    @Transactional
    public EventFullDto cancel(Long eventId, Long userId) {
        Event event = getEvent(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Только создатель события может его отменить.");
        }

        if (State.PENDING.equals(event.getState())) {
            event.setState(State.CANCELED);
        } else {
            throw new NotValidException("Only pending or canceled events can be changed");
        }

        event = repository.save(event);
        return mapper.eventToEventFullDto(event);
    }

    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = getEvent(eventId);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new NotValidException("Дата и время события не могут быть раньше, чем через час" +
                    " от текущего момента");
        }

        if (State.PENDING.equals(event.getState())) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            throw new NotValidException("Only pending events can be changed");
        }
        event = repository.save(event);
        return mapper.eventToEventFullDto(event);
    }

    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = getEvent(eventId);

        if (State.PENDING.equals(event.getState())) {
            event.setState(State.CANCELED);
        } else {
            throw new NotValidException("Only pending events can be changed");
        }
        event = repository.save(event);
        return mapper.eventToEventFullDto(event);
    }

    public EventFullDto getEventByIdForPublic(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found."));

        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Event not published.");
        }
        return mapper.eventToEventFullDto(event);
    }

    public EventShortDto getEventShortDtoByIdForPublic(Long eventId) {
        Event event = getEvent(eventId);

        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Event not published.");
        }
        return mapper.eventToEventShortDto(event);
    }

    public EventFullDto getEventByIdForUser(Long eventId, Long userId) {

        Event event = getEvent(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Event with id=" + eventId + " created other user");
        }
        return mapper.eventToEventFullDto(event);
    }

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        Pageable page = getPage(from, size, "id", Sort.Direction.ASC);
        return repository.findByInitiatorId(userId, page).stream()
                .map(mapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

/*    public Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found."));
    }*/

    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {

        Event event = getEvent(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Отредактировать событие может только инициатор!");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (updateEventRequest.getCategoryId() != null) {
            event.setCategory(categoryMapper.categoryDtoToCategory(categoryService.getCategoryById(updateEventRequest.getCategoryId())));
        }

        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }

        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }

        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }

        event.setState(State.PENDING);
        event = repository.save(event);

        return mapper.eventToEventFullDto(event);
    }

/*    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest,
                                           Category category) {

        Event event = getEvent(eventId);
        log.info("Получено событие для редактирования {}", event);

        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }

        if (adminUpdateEventRequest.getCategoryId() != null) {
            event.setCategory(category);
        }

        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }

        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }

        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLon(adminUpdateEventRequest.getLocation().getLon());
            event.setLat(adminUpdateEventRequest.getLocation().getLat());
        }

        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }

        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }

        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }

        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }

        event = repository.save(event);

        return mapper.eventToEventFullDto(event);
    }*/

    public List<EventShortDto> getEventsForPublic(String text, Set<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, EventSort sort, Integer from, Integer size) {

        if (rangeStart.isAfter(rangeEnd)) {
            throw new NotValidException("Дата и время окончаний события не может быть раньше даты начала событий!");
        }

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("stateFilter").setParameter("state", State.PUBLISHED.toString());

        Filter dateFilter = session.enableFilter("dateFilter");
        dateFilter.setParameter("rangeStart", rangeStart);
        dateFilter.setParameter("rangeEnd", rangeEnd);

        if (paid != null) {
            session.enableFilter("paidFilter").setParameter("paid", paid);
        }

        List<Event> events;

        if (categories != null) {
            events = repository.findByCategoryIdsAndText(text, categories);
        } else {
            events = repository.findByText(text);
        }

        if (paid != null) {
            session.disableFilter("paidFilter");
        }
        session.disableFilter("dateFilter");
        session.disableFilter("stateFilter");

        List<EventShortDto> eventShortDtos = events.stream()
                .map(mapper::eventToEventShortDto)
                .collect(Collectors.toList());

        if (onlyAvailable) {
            eventShortDtos = eventShortDtos.stream()
                    .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (EventSort.VIEWS.equals(sort)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }
/*
    public List<EventFullDto> getEventsForAdmin(Set<Long> users, Set<State> states, Set<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {

        if (rangeStart.isAfter(rangeEnd)) {
            throw new NotValidException("Дата и время окончаний события не может быть раньше даты начала событий!");
        }

        if (states == null) {
            states = new HashSet<>();
            states.add(State.PENDING);
            states.add(State.CANCELED);
            states.add(State.PUBLISHED);
        }

        Session session = entityManager.unwrap(Session.class);

        // включаем фильтр по датам
        Filter dateFilter = session.enableFilter("dateFilter");
        dateFilter.setParameter("rangeStart", rangeStart);
        dateFilter.setParameter("rangeEnd", rangeEnd);

        List<Event> events = new ArrayList<>();

        Pageable page = getPage(from, size, "id", Sort.Direction.ASC);

        if ((categories != null) && (users != null)) {
            events = repository.findByUsersAndCategoriesAndStates(users, categories, states, page);
        }

        if ((categories == null) && (users == null)) {
            events = repository.findByStates(states, page);
        }

        if ((categories != null) && (users == null)) {
            events = repository.findByCategoriesAndStates(categories, states, page);
        }

        if ((categories == null) && (users != null)) {
            events = repository.findByUsersAndStates(users, states, page);
        }

        session.disableFilter("dateFilter");

        List<EventFullDto> eventFullDtos = events.stream()
                .map(mapper::eventToEventFullDto)
                .collect(toList());

        return eventFullDtos;
    }*/

    public List<EventShortDto> getEventsByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId).stream()
                .map(mapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    public Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found."));
    }
}