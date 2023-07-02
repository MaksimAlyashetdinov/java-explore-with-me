package ru.practicum.request.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.StateRequest;
import ru.practicum.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    public List<Request> findByEventId(Integer eventId);

    public List<Request> findByEventIdAndStatus(Integer eventId, StateRequest status);

    List<Request> findAllByIdIn(List<Integer> ids);

    List<Request> findByRequesterId(Integer requesterId);

    Optional<Request> findByRequesterIdAndEventId(Integer requesterId, Integer eventId);
}