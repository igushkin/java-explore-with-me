package ru.practicum.ewm.admin_access.service.compilation;

import ru.practicum.ewm.common.dto.compilation.CompilationDto;
import ru.practicum.ewm.common.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.common.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}