package ru.practicum.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statRepository.save(EndpointHitMapper.mapToEndpointHit(endpointHitDto));
        log.info("Save in stat {}", endpointHit);
        return endpointHitDto;
    }

    @Override
    public List<ViewStatsDto> findAll(LocalDateTime start, LocalDateTime end, List<String> uris,
            Boolean unique) {
        List<ViewStats> viewStatsFromRepository;
        if (unique) {
            viewStatsFromRepository = statRepository.findUniqueViewStats(start, end, uris);
            log.info("Get unique view stats: {}", viewStatsFromRepository);
        } else {
            viewStatsFromRepository = statRepository.findAllViewStats(start, end, uris);
            log.info("Get all view stats: {}", viewStatsFromRepository);
        }
        return new ArrayList<>(viewStatsFromRepository.stream().map(ViewStatsMapper::mapToViewStatsDto).collect(
                Collectors.toList()));
    }
}