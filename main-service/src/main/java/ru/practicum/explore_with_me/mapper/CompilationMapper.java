package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.entity.Compilation;
import ru.practicum.explore_with_me.service.EventService;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CompilationMapper {

    private final EventMapper eventMapper;
    private final EventService eventService;

    public Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto) {

        Compilation compilation = Compilation.builder()
                .id(null)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(newCompilationDto.getEvents().stream()
                        .filter(Objects::nonNull)
                        .map(eventService::getEvent)
                        .collect(Collectors.toSet()))
                .build();

        return compilation;
    }

    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents().stream()
                        .map((x) -> eventMapper.eventToEventShortDto(x))
                        .collect(Collectors.toSet()))
                .build();
        return compilationDto;
    }
}