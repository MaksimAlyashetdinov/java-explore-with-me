package ru.practicum.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM EndpointHit as hit " +
            "WHERE hit.timestamp BETWEEN :start AND :end " +
            "AND (hit.uri IN :uriList OR :uriList IS NULL) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end, List<String> uriList);

    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM EndpointHit as hit " +
            "WHERE hit.timestamp BETWEEN :start AND :end " +
            "AND (hit.uri IN :uriList OR :uriList IS NULL) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStats> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uriList);
}