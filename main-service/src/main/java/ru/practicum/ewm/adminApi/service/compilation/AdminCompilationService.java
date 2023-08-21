package ru.practicum.ewm.adminApi.service.compilation;

import ru.practicum.ewm.base.dto.Compilation.CompilationDto;
import ru.practicum.ewm.base.dto.Compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.Compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
