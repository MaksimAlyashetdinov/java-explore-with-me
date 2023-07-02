package ru.practicum.request.service;

import java.util.List;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

public interface RequestService {

    List<ParticipationRequestDto> getEventsWithUserRequest(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Integer userId, Integer eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getUserRequests(Integer userId);

    ParticipationRequestDto create(Integer userId, Integer eventId);

    ParticipationRequestDto cancel(Integer userId, Integer requestId);
}