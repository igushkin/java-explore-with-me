package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.entity.Category;
import ru.practicum.explore_with_me.entity.Event;
import ru.practicum.explore_with_me.entity.User;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.model.Status;
import ru.practicum.explore_with_me.service.RequestService;
import ru.practicum.explore_with_me.service.StatsService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EventMapper {

    private final RequestService requestService;
    private final StatsService statsService;

    /*    public EventCreateDto newEventDtoToEventCreateDto(NewEventDto newEventDto,
                                                          UserDto initiator, CategoryDto categoryDto) {
            if (newEventDto == null) {
                return null;
            }

            EventCreateDto eventCreateDto = EventCreateDto.builder()
                    .annotation(newEventDto.getAnnotation())
                    .categoryDto(categoryDto)
                    .description(newEventDto.getDescription())
                    .eventDate(newEventDto.getEventDate())
                    .initiator(initiator)
                    .paid(newEventDto.isPaid())
                    .location(newEventDto.getLocation())
                    .participantLimit(newEventDto.getParticipantLimit())
                    .requestModeration(newEventDto.isRequestModeration())
                    .title(newEventDto.getTitle())
                    .createdOn(LocalDateTime.now())
                    .build();
            return eventCreateDto;
        }
*/
    public Event newEventDtoToEvent(NewEventDto newEventDto, CategoryDto categoryDto, UserDto initiator) {
        if (newEventDto == null) {
            return null;
        }

        Event event = Event.builder()
                .id(null)
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .category(new Category(categoryDto.getId(), categoryDto.getName()))
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .initiator(new User(initiator.getId(), initiator.getEmail(), initiator.getName()))
                .createdOn(LocalDateTime.now())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .build();

        return event;
    }

    public EventFullDto eventToEventFullDto(Event event) {

        Long confirmedRequests =
                (long) requestService.getRequestByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
        Long views = statsService.getViews("/events/" + event.getId());

        EventFullDto eventFullDto = EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();

        return eventFullDto;
    }

    public EventShortDto eventToEventShortDto(Event event) {

        Long confirmedRequests = (long) requestService.getRequestByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
        Long views = statsService.getViews("/events/" + event.getId());

        EventShortDto eventShortDto = EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .participantLimit(event.getParticipantLimit())
                .build();

        return eventShortDto;
    }
}