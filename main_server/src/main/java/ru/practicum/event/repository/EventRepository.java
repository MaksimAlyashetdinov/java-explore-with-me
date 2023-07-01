package ru.practicum.event.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.EventState;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByInitiatorId(Integer initiatorId, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutStates(List<Integer> users, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.state IN :states and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithStates(List<Integer> users, List<EventState> states, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findAllWithFilter(String text, List<Integer> categories, Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.state IN :states and " +
            "e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiator(List<EventState> states, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.state IN :states and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutCategory(List<Integer> users, List<EventState> states, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiatorAndStates(List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.state IN :states and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutInitiatorAndCategory(List<EventState> states, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.initiator.id IN :users and " +
            "e.eventDate between :start and :end")
    List<Event> searchEventsWithoutStatesAndCategory(List<Integer> users, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e")
    List<Event> searchEventsWithoutInitiatorAndStatesAndCategory(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findAllWithoutText(List<Integer> categories, Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findAllWithoutCategory(String text, Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.paid = :paid and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findAllWithoutTextAndCategory(Boolean paid, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findAllWithoutTextAndCategoryAndPaid(LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state")
    List<Event> findAllWithoutTextAndPaid(List<Integer> categories, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findAllWithoutCategoryAndPaid(String text, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);

    @Query("SELECT e from Event e " +
            "where e.category.id IN :categories and " +
            "e.eventDate between :start and :end and " +
            "e.state = :state and " +
            "(e.annotation like %:text% or e.description like %:text%)")
    List<Event> findAllWithFilterWithoutPaid(String text, List<Integer> categories, LocalDateTime start,
            LocalDateTime end, EventState state, Pageable pageable);
}