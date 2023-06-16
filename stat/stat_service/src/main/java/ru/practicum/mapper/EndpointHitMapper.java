package ru.practicum.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                          .id(endpointHitDto.getId())
                          .app(endpointHitDto.getApp())
                          .uri(endpointHitDto.getUri())
                          .ip(endpointHitDto.getIp())
                          .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                                  .build();
    }

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                             .id(endpointHit.getId())
                             .app(endpointHit.getApp())
                             .uri(endpointHit.getUri())
                             .ip(endpointHit.getIp())
                             .timestamp(endpointHit.getTimestamp().toString())
                             .build();
    }
}