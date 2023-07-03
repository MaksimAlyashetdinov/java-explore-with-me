package ru.practicum.event.dto;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.Location.model.Location;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventState;
import ru.practicum.user.dto.UserShortDto;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    Long id;
    String annotation;
    Category category;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
    UserShortDto initiator;
    LocalDateTime createdOn;
    @Enumerated(EnumType.STRING)
    EventState state;
    Integer confirmedRequests;
    LocalDateTime publishedOn;
    Long views;
}