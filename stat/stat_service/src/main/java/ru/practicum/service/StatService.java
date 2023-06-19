package ru.practicum.service;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

public interface StatService {

    EndpointHitDto save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}