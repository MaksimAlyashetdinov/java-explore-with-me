package ru.practicum.request.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.StateRequest;
import ru.practicum.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

    public List<Request> findByEventId(Long eventId);

    public List<Request> findByEventIdAndStatus(Long eventId, StateRequest status);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findByRequesterId(Long requesterId);

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);
}