package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewStatsMapper {

    public static ViewStats mapToViewStats(ViewStatsDto viewStatsDto) {
        return ViewStats.builder()
                        .app(viewStatsDto.getApp())
                        .uri(viewStatsDto.getUri())
                        .hits(viewStatsDto.getHits())
                        .build();
    }

    public static ViewStatsDto mapToViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                           .app(viewStats.getApp())
                           .uri(viewStats.getUri())
                           .hits(viewStats.getHits())
                           .build();
    }
}