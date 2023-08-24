package ru.practicum.ewm.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.common.entity.Category;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository {


    boolean existsByCategory(Category category);


    Optional<Event> findByIdAndInitiatorId(Long id, Long userId);

    List<Event> findAllByIdIn(List<Long> ids);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL or e.initiator.id IN (:users)) " +
            "AND (:states IS NULL or e.state IN (:states)) " +
            "AND (:categories IS NULL or e.category.id IN (:categories)) " +
            "AND ((cast(:rangeStart AS timestamp ) IS NULL AND (cast(:rangeEnd AS timestamp ) IS NULL)) OR e.date BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> findEventsByParams(@Param("users") List<Long> users,
                                   @Param("states") List<State> states,
                                   @Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable pageable);

    boolean existsByIdAndInitiatorId(Long id, Long userId);
}