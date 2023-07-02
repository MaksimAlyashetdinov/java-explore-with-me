package ru.practicum.event.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;

public interface EventService {

    List<Event> getEventsByUserId(Long userId, int from, int size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId,
            UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getFilteredEvents(List<Long> users, List<String> states,
            List<Long> categories, String rangeStart, String rangeEnd, Integer from,
            Integer size);

    EventFullDto adminEventUpdate(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
            String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
            Integer size,
            HttpServletRequest request);

    EventFullDto getByIdWithCount(Long id, HttpServletRequest request);
}