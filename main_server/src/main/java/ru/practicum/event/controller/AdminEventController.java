package ru.practicum.event.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getFilteredEvents(@RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @Positive @RequestParam(defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getFilteredEvents(users,states,categories,rangeStart,rangeEnd,from,size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto adminEventUpdate(@Positive @PathVariable Integer eventId,
            @RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.adminEventUpdate(eventId,updateEventAdminRequest);
    }
}