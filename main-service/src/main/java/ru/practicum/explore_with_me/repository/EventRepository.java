package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.entity.Event;
import ru.practicum.explore_with_me.model.State;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(Long userId, Pageable page);

    List<Event> findByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%'))")
    List<Event> findByCategoryIdsAndText(String text, Set<Long> categories);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%'))")
    List<Event> findByText(String text);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(e.initiator.id IN :users) AND " +
            "(e.state IN :states)")
    List<Event> findByUsersAndCategoriesAndStates(List<Long> users, List<Long> categories,
                                                  List<State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.initiator.id IN :users) AND " +
            "(e.state IN :states)")
    List<Event> findByUsersAndStates(List<Long> users, List<State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(e.state IN :states)")
    List<Event> findByCategoriesAndStates(List<Long> categories, List<State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.state IN :states)")
    List<Event> findByStates(List<State> states, Pageable pageable);
}