package ru.practicum.event.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.EventState;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutStates(List<Long> users, List<Long> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.state IN :states and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithStates(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findWithFilter(String text, List<Long> categories, Boolean paid,
            LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.state IN :states and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiator(List<EventState> states, List<Long> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.state IN :states and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutCategory(List<Long> users, List<EventState> states, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiatorAndStates(List<Long> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.state IN :states and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiatorAndCategory(List<EventState> states, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutStatesAndCategory(List<Long> users, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e")
    List<Event> searchEventsWithoutInitiatorAndStatesAndCategory(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findWithoutText(List<Long> categories, Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findWithoutCategory(String text, Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findWithoutTextAndCategory(Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findWithoutTextAndCategoryAndPaid(LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findWithoutTextAndPaid(List<Long> categories, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findWithoutCategoryAndPaid(String text, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findWithFilterWithoutPaid(String text, List<Long> categories, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);
}