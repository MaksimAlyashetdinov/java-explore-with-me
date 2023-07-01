package ru.practicum.request.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.StateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getEventsWithUserRequest(Integer userId, Integer eventId) {
        containsUser(userId);
        containsEvent(eventId);
        log.info("Get requests for event with id {}", eventId);
        List<Request> requests = requestRepository.findByEventId(eventId);
        return requests.stream()
                       .map(RequestMapper::mapToParticipationRequestDto)
                       .collect(
                               Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Integer userId, Integer eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        containsUser(userId);
        containsEvent(eventId);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        Event event = eventRepository.findById(eventId)
                                     .get();
        List<Request> confirmedRequests = requestRepository.findByEventIdAndStatus(event.getId(),
                StateRequest.CONFIRMED);
        validateEvent(event, confirmedRequests);
        int limit = event.getParticipantLimit() - confirmedRequests.size();
        List<Request> requests = requestRepository.findAllByIdIn(
                eventRequestStatusUpdateRequest.getRequestIds());
        for (Request request : requests) {
            if (!request.getStatus()
                        .equals(StateRequest.PENDING)) {
                throw new ValidationException(
                        "You can't change status for request with id " + request.getId());
            }
            if (limit > 0 && eventRequestStatusUpdateRequest.getStatus()
                                                            .equals(StateRequest.CONFIRMED)) {
                request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                requestRepository.saveAndFlush(request);
                limit--;
                confirmed.add(RequestMapper.mapToParticipationRequestDto(request));
            } else {
                request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                requestRepository.saveAndFlush(request);
                rejected.add(RequestMapper.mapToParticipationRequestDto(request));
            }
        }
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmed);
        event.setConfirmedRequests(event.getConfirmedRequests() + confirmed.size());
        eventRepository.saveAndFlush(event);
        eventRequestStatusUpdateResult.setRejectedRequests(rejected);
        log.info("Update request status.");
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Integer userId) {
        containsUser(userId);
        List<Request> userRequests = requestRepository.findByRequesterId(userId);
        log.info("Get user requests.");
        return userRequests.stream()
                           .map(RequestMapper::mapToParticipationRequestDto)
                           .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto create(Integer userId, Integer eventId) {
        containsUser(userId);
        containsEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        User requester = userRepository.findById(userId).get();
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(
                    "It is not possible to submit a repeated request for participation.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(
                    "You cannot submit a request to participate in your own event.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(
                    "You cannot submit a request to participate in an unpublished event.");
        }
        if (event.getParticipantLimit() != 0
                && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException(
                    "The limit of participants of the event has already been reached.");
        }
        Request newRequest = Request.builder()
                                    .requester(requester)
                                    .event(event)
                                    .status(StateRequest.CONFIRMED)
                                    .created(LocalDateTime.now())
                                    .build();
        if (event.getRequestModeration() == true && event.getParticipantLimit() != 0) {
            newRequest.setStatus(StateRequest.PENDING);
        } else {
            newRequest.setStatus(StateRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        Request savedRequest = requestRepository.save(newRequest);
        log.info("Create request:" + savedRequest);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancel(Integer userId, Integer requestId) {
        containsUser(userId);
        containsRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        request.setStatus(StateRequest.CANCELED);
        Request savedRequest = requestRepository.saveAndFlush(request);
        log.info("Cancel request: " + savedRequest);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
    }

    private void containsUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found.");
        }
    }

    private void containsEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id " + eventId + " not found.");
        }
    }

    private void containsRequest(Integer requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new ValidationException("Request with id " + requestId + " not found.");
        }
    }

    private void validateEvent(Event event, List<Request> confirmedRequests) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ValidationException("Confirmation of applications is not required.");
        }
        if (event.getParticipantLimit() == confirmedRequests.size()) {
            throw new ConflictException("The limit of participants of the event has been reached.");
        }
    }
}