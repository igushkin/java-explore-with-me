package ru.practicum.ewm.admin_access.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.compilation.CompilationDto;
import ru.practicum.ewm.common.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.common.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.common.entity.Compilation;
import ru.practicum.ewm.common.entity.Event;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.CompilationMapper;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.repository.EventRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toEntity(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));
        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add compilation: {}", compilation.getTitle());
        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        if (compilationRepository.existsById(compId)) {
            log.info("Deleted compilation with id = {}", compId);
            compilationRepository.deleteById(compId);
        }
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilationTarget = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation not found with id = %s", compId)));

        BeanUtils.copyProperties(dto, compilationTarget, "events", "pinned", "title");

        compilationTarget.setEvents(findEvents(dto.getEvents()));
        try {
            compilationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Update category: {}", compilationTarget.getTitle());
        return CompilationMapper.toDto(compilationTarget);
    }

    private List<Event> findEvents(List<Long> eventsId) {
        if (eventsId == null) {
            return Collections.emptyList();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }
}