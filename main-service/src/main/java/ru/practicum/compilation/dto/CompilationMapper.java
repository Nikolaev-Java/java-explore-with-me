package ru.practicum.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.Compilation;
import ru.practicum.event.Event;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation source);

    List<CompilationDto> ocompilationDtoList(List<Compilation> sources);

    @Mapping(source = "events", target = "events", qualifiedByName = "eventsIdToEvents")
    Compilation toCompilation(NewCompilationDto source);

    default Set<EventShortDto> eventToShortDtoList(Set<Event> source) {
        return Mappers.getMapper(EventMapper.class).toShortDtoSet(source);
    }

    @Named(value = "eventsIdToEvents")
    static Set<Event> eventsIdToEvents(Set<Integer> source) {
        if (source == null || source.isEmpty())
            return new HashSet<>();
        return source.stream()
                .map(it -> Event.builder().id(it).build()).collect(Collectors.toSet());
    }
}
