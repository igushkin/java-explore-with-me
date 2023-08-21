package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.entity.Compilation;
import ru.practicum.explore_with_me.entity.ParticipationRequest;
import ru.practicum.explore_with_me.model.Status;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, Status status);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    ParticipationRequest findByRequesterIdAndEventId(Long userId, Long eventId);
}