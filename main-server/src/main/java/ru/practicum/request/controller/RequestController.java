package ru.practicum.request.controller;

import java.util.List;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Integer userId) {
        log.info("Get user requests.");
        return requestService.getUserRequests(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addUserRequest(@Positive @PathVariable Integer userId,
            @RequestParam @Positive Integer eventId) {
        log.info("Add new request.");
        return requestService.create(userId, eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@Positive @PathVariable Integer userId,
            @Positive @PathVariable Integer eventId,
            @RequestBody(required = false) @Validated
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Update request.");
        return requestService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventsWithUserRequest(
            @Positive @PathVariable Integer userId,
            @Positive @PathVariable Integer eventId) {
        log.info("Get events with user request.");
        return requestService.getEventsWithUserRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto deleteUserRequest(@PathVariable Integer userId,
            @PathVariable Integer requestId) {
        log.info("Delete request.");
        return requestService.cancel(userId, requestId);
    }
}