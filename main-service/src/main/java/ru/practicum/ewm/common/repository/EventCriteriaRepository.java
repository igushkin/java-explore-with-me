package ru.practicum.ewm.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.entity.EventSearchCriteria;

@Repository
public interface EventCriteriaRepository {

    Page<Event> findAllWithFilters(Pageable pageable, EventSearchCriteria eventSearchCriteria);
}