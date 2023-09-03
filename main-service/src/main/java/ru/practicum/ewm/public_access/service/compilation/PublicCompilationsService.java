package ru.practicum.ewm.public_access.service.compilation;

import ru.practicum.ewm.common.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto get(Long comId);
}