package ru.practicum.ewm.common.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.common.dto.Compilation.CompilationDto;
import ru.practicum.ewm.common.dto.Compilation.NewCompilationDto;
import ru.practicum.ewm.common.dto.Compilation.UpdateCompilationRequest;
import ru.practicum.ewm.common.entity.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CompilationMapper {

    public static Compilation toEntity(NewCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .build();
    }

    public static Compilation toEntity(UpdateCompilationRequest dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .build();
    }

    public static CompilationDto toDto(Compilation entity) {
        return CompilationDto.builder()
                .id(entity.getId())
                .pinned(entity.isPinned())
                .title(entity.getTitle())
                .events(EventMapper.toEventShortDtoList((entity.getEvents())))
                .build();
    }

    public static List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }
}