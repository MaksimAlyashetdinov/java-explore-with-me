package ru.practicum.event.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersEventController {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<Event> getEventsByUserId(@PathVariable @NotNull Integer userId, @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Get events by user id request.");
        return eventService.getEventsByUserId(userId,from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @NotNull Integer userId, @RequestBody @NotNull @Valid NewEventDto newEventDto) {
        log.info("Add event request.");
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable @NotNull Integer userId, @PathVariable @NotNull Integer eventId) {
        log.info("Get event by event id request.");
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable @NotNull Integer userId, @PathVariable @NotNull Integer eventId, @RequestBody @NotNull @Valid
            UpdateEventUserRequest updateEventUserRequest) {
        log.info("Update event request.");
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }
}
