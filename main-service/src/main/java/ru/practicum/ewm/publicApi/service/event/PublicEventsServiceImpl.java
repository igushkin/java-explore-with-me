package ru.practicum.ewm.publicApi.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.EventShortDto;
import ru.practicum.ewm.base.entity.Event;
import ru.practicum.ewm.base.entity.EventSearchCriteria;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.util.page.MyPageRequest;
import ru.practicum.ewm.publicApi.dto.RequestParamForEvent;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final StatsClient statsClient;

    @Value("${ewm.service.name}")
    private String serviceName;

    @Transactional
    @Override
    public List<EventShortDto> getAll(RequestParamForEvent param) {
        MyPageRequest pageable = createPageable(param.getSort(), param.getFrom(), param.getSize());
        EventSearchCriteria eventSearchCriteria = createCriteria(param);

        List<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository
                .findAllWithFilters(pageable, eventSearchCriteria).toList());

        log.info("Get events list size: {}", eventShorts.size());
        saveEndpointHit(param.getRequest());
        return eventShorts;
    }


    @Transactional
    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id=%d is not published", id));
        }

        saveEndpointHit(request);
        log.info("Get event: {}", event.getId());
        var views = statsClient.getStats(LocalDateTime.now().minusYears(1000), LocalDateTime.now().plusYears(1000), List.of(request.getRequestURI()), true).get(0).getHits();
        event.setViews(views);
        eventRepository.flush();
        return EventMapper.toEventFullDto(event);
    }

    private void saveEndpointHit(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.hit(hitDto);
    }

    private MyPageRequest createPageable(String sort, int from, int size) {
        MyPageRequest pageable = null;
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "event_date"));
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "views"));
        }
        return pageable;
    }

    private EventSearchCriteria createCriteria(RequestParamForEvent param) {
        return EventSearchCriteria.builder()
                .text(param.getText())
                .categories(param.getCategories())
                .rangeEnd(param.getRangeEnd())
                .rangeStart(param.getRangeStart())
                .paid(param.getPaid())
                .build();
    }
}