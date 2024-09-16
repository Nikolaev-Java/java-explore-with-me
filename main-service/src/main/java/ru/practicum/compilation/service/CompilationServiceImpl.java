package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.CompilationRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.event.Event;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    public static final String ERROR_NOT_FOUND_MSG = "Can't find compilation with id ";
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> getAllCompilation(Boolean pinned, Pageable page) {
        log.debug("Received request all compilation");
        List<Compilation> content;
        if (Objects.isNull(pinned)) {
            content = repository.findAll(page).getContent();
        } else {
            content = repository.findByPinned(pinned, page);
        }
        log.debug("Found {} compilations", content.size());
        return mapper.ocompilationDtoList(content);
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        log.debug("Received request getting compilation with id {}", compId);
        Compilation compilation = findCompilationById(compId);
        log.debug("Found compilation: {}", compilation);
        return mapper.toCompilationDto(compilation);
    }


    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        log.debug("Received request creating compilation: {}", compilationDto);
        Compilation saved = repository.save(mapper.toCompilation(compilationDto));
        log.debug("Saved: {}", saved);
        return mapper.toCompilationDto(saved);
    }

    @Override
    public void deleteById(long compId) {
        log.debug("Received request deleting compilation with id {}", compId);
        if (!repository.existsById(compId)) {
            throw new NotFoundException(ERROR_NOT_FOUND_MSG + compId);
        }
        log.debug("Deleting compilation with id {}", compId);
        repository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto update(long compId, UpdateCompilationDto compilationDto) {
        log.debug("Received request updating compilation with id {}", compId);
        Compilation compilationToUpdate = findCompilationById(compId);
        if (compilationDto.getEvents() != null) {
            Set<Event> eventSet = compilationDto.getEvents().stream()
                    .map(integer -> Event.builder().id(integer).build())
                    .collect(Collectors.toSet());
            compilationToUpdate.setEvents(eventSet);
        }
        if (compilationDto.getTitle() != null) {
            compilationToUpdate.setTitle(compilationDto.getTitle());
        }
        compilationToUpdate.setPinned(compilationDto.isPinned());
        Compilation saved = repository.save(compilationToUpdate);
        log.debug("Update: {}", saved);
        return mapper.toCompilationDto(saved);
    }

    private Compilation findCompilationById(long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_MSG + compId));
    }
}
