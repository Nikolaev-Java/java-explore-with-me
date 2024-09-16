package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ClientService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.utils.PublicEventsFilters;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService service;
    private final ClientService client;

    @GetMapping
    public List<EventShortDto> getAllEvents(@ModelAttribute("filters") @Valid PublicEventsFilters filters,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {

        List<EventShortDto> eventShortDtos = service.publicGetAllEvents(filters, PageRequest.of(from, size), request);
        return eventShortDtos;
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        EventFullDto eventFullDto = service.publicGetEvent(id, request);
        return eventFullDto;
    }
}
