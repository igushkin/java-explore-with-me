package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.dto.UpdateCompilationDto;
import ru.practicum.explore_with_me.entity.Compilation;
import ru.practicum.explore_with_me.exception.AlreadyExistsException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.util.UtilityClass.getPage;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        try {
            Compilation compilation = compilationMapper.newCompilationDtoToCompilation(newCompilationDto);
            compilation = compilationRepository.save(compilation);
            log.info("Создана подборка: {}", compilation.toString());
            return compilationMapper.compilationToCompilationDto(compilation);

        } catch (Exception e) {
            throw new AlreadyExistsException("Подборка с названием '" + newCompilationDto.getTitle() + "' уже существует!");
        }
    }

    @Transactional
    public CompilationDto update(Long id, UpdateCompilationDto updateCompilationDto) {

        Compilation compilation = null;

        try {
            compilation = compilationRepository.findById(id).get();
        } catch (Exception e) {

        }


    }
/*
    @Transactional
    public CompilationDto pinUnpin(Long compId, boolean pin) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilation.setPinned(pin);
        return mapper.compilationToCompilationDto(repository.save(compilation));
    }

    @Transactional
    public void delete(Long compId) {
        try {
            repository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
    }

    @Transactional
    public void deleteEvent(Long compId, Event event) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilation.getEvents().remove(event);
        repository.save(compilation);
    }

    @Transactional
    public CompilationDto addEvent(Long compId, Event event) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilation.getEvents().add(event);
        return mapper.compilationToCompilationDto(repository.save(compilation));
    }
*/


    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));

        return compilationMapper.compilationToCompilationDto(compilation);
    }


    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        Pageable page = getPage(from, size, "id", Sort.Direction.ASC);

        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, page)
                    .stream()
                    .map(compilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(page).stream()
                    .map(compilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        }
    }
}