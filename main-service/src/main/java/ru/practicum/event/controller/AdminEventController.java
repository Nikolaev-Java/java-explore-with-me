package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateAdminEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.utils.AdminEventsFilters;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getAll(@ModelAttribute("filters") AdminEventsFilters filters,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        return service.adminGetAllEvents(filters, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId, @Valid @RequestBody UpdateAdminEventDto dto) {
        return service.adminUpdateEvent(eventId, dto);
    }
}
