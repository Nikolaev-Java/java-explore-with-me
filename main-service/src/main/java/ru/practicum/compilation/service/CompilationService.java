package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilation(Boolean pinned, Pageable page);

    CompilationDto getCompilationById(long compId);

    CompilationDto create(NewCompilationDto compilationDto);

    void deleteById(long compId);

    CompilationDto update(long compId, UpdateCompilationDto compilationDto);
}
