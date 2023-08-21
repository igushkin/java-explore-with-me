package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.entity.Event;
import ru.practicum.explore_with_me.entity.ParticipationRequest;
import ru.practicum.explore_with_me.entity.User;
import ru.practicum.explore_with_me.exception.ForbiddenException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.RequestMapper;
import ru.practicum.explore_with_me.model.Status;
import ru.practicum.explore_with_me.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper mapper;

/*    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.repository = requestRepository;
        this.mapper = requestMapper;
    }

    public List<ParticipationRequestDto> getRequestByUserId(Long userId) {
        return repository.findAllByRequesterId(userId).stream()
                .map(mapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }*/

    public List<ParticipationRequestDto> getRequestByEventIdAndStatus(Long eventId, Status status) {
        return repository.findAllByEventIdAndStatus(eventId, status).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }


    public ParticipationRequestDto getRequestByUserIdAndEventId(Long userId, Long eventId) {
        return mapper.toParticipationRequestDto(repository.findByRequesterIdAndEventId(userId, eventId));
    }

    @Transactional
    public ParticipationRequestDto createRequest(User user, Event event) {
        if (event.getParticipantLimit() != 0) {
            Integer participants = getRequestByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
            log.info("Текущее количество подтвержденных участников = {}", participants);

            if (event.getParticipantLimit() <= participants) {
                throw new ForbiddenException("У события достигнут лимит запросов на участие!");
            }
        }

        ParticipationRequest participationRequest = new ParticipationRequest(null, LocalDateTime.now(), Status.PENDING, event, user);

        if (!event.getRequestModeration()) {
            participationRequest.setStatus(Status.CONFIRMED);
        }

        log.info("Создаем новый запрос {}", participationRequest);
        participationRequest = repository.save(participationRequest);
        log.info("Запрос на участие создан {}", participationRequest);
        return mapper.toParticipationRequestDto(participationRequest);
    }


    @Transactional
    public ParticipationRequestDto cancelRequest(User user, Long requestId) {
        ParticipationRequest participationRequest = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ParticipationRequest with id=" + requestId + " not found."));
        log.info("Получили запрос {}", participationRequest);
        if (participationRequest.getRequester().getId().equals(user.getId())) {
            participationRequest.setStatus(Status.CANCELED);
        } else {
            throw new ForbiddenException("Пользователь с ID= " + user.getId() + " не подавал заявку на участие " +
                    "в событии с ID= " + participationRequest.getEvent().getId());
        }
        participationRequest = repository.save(participationRequest);
        log.info("Запрос на участие отменен {}", participationRequest);
        return mapper.toParticipationRequestDto(participationRequest);
    }

    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        List<ParticipationRequest> requests = repository.findAllByRequesterId(userId);
        log.info("Получен список заявок на участия пользователя с ID= {}", userId);
        return requests.stream()
                .map(mapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /*
          @Transactional
          public ParticipationRequestDto confirmRequest(Event event, Long requestId) {

              ParticipationRequest participationRequest = repository.findById(requestId)
                      .orElseThrow(() -> new NotFoundException("ParticipationRequest with id=" + requestId + " not found."));
              log.info("Получили запрос {}", participationRequest);

              Integer participants = 0;

              if (event.getParticipantLimit() != 0) {
                  participants = getRequestByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
                  log.info("Текущее количество подтвержденных участников = {}", participants);

                  if (event.getParticipantLimit() <= participants) {
                      throw new ForbiddenException("У события достигнут лимит запросов на участие!");
                  }
              }

              participationRequest.setStatus(Status.CONFIRMED);
              participationRequest = repository.save(participationRequest);
              log.info("Запрос на участие подтвержден {}", participationRequest);

              if (event.getParticipantLimit().equals(++participants)) {
                  log.info("Текущее количество подтвержденных участников = {}", participants);

                  List<ParticipationRequest> requestsToReject = repository.findAllByEventIdAndStatus(event.getId(),
                          Status.PENDING);

                  requestsToReject.forEach(r -> r.setStatus(Status.REJECTED));

                  repository.saveAll(requestsToReject);
              }

              return mapper.toParticipationRequestDto(participationRequest);
          }

          @Transactional
          public ParticipationRequestDto rejectRequest(Event event, Long requestId) {
              ParticipationRequest participationRequest = repository.findById(requestId)
                      .orElseThrow(() -> new NotFoundException("ParticipationRequest with id=" + requestId + " not found."));
              log.info("Получили запрос {}", participationRequest);

              participationRequest.setStatus(Status.REJECTED);
              participationRequest = repository.save(participationRequest);
              log.info("Запрос на участие отклонен {}", participationRequest);

              return mapper.toParticipationRequestDto(participationRequest);
          }
      */
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        List<ParticipationRequest> requests = repository.findAllByEventId(eventId);
        log.info("Получен список заявок на участие в событии с ID= {}", eventId);

        return requests.stream()
                .map(mapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}