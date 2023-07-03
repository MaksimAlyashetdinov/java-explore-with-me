package ru.practicum.compilation.service;

import java.util.List;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    List<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation getById(Long compId);

    void deleteById(Long compId);

    Compilation update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
