package ru.practicum.ewm.admin_access.service.compilation;

import ru.practicum.ewm.common.dto.Compilation.CompilationDto;
import ru.practicum.ewm.common.dto.Compilation.NewCompilationDto;
import ru.practicum.ewm.common.dto.Compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}