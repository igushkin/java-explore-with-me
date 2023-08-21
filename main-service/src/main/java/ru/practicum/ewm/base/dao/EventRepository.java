package ru.practicum.ewm.base.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository  {


    boolean existsByCategory(Category category);


    Optional<Event> findByIdAndInitiatorId(Long id, Long userId);

    Set<Event> findAllByIdIn(Set<Long> ids);


    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN (:users) " +
            "AND e.state IN (:states) " +
            "AND e.category.id IN (:categories) " +
            "AND e.date BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findEventsByParams(
            @Param("users") List<Long> users,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    boolean existsByIdAndInitiatorId(Long id, Long userId);

}
