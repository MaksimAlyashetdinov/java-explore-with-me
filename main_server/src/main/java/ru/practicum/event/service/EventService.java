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

    List<Event> getEventsByUserId(Integer userId, int from, int size);

    EventFullDto addEvent(Integer userId, NewEventDto newEventDto);

    EventFullDto getEvent(Integer userId, Integer eventId);

    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getFilteredEvents(List<Integer> users, List<String> states,List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto adminEventUpdate(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAll(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
            HttpServletRequest request);

    EventFullDto getByIdWithCount(Integer id, HttpServletRequest request);
}