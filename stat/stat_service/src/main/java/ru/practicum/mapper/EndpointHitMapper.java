package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {

    public static EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                          .id(endpointHitDto.getId())
                          .app(endpointHitDto.getApp())
                          .uri(endpointHitDto.getUri())
                          .ip(endpointHitDto.getIp())
                          .timestamp(endpointHitDto.getTimestamp())
                          .build();
    }

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                             .id(endpointHit.getId())
                             .app(endpointHit.getApp())
                             .uri(endpointHit.getUri())
                             .ip(endpointHit.getIp())
                             .timestamp(endpointHit.getTimestamp())
                             .build();
    }
}