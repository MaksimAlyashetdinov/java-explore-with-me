package ru.practicum.comment.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filter {

    List<Long> commentsIds;
    List<Long> userIds;
    List<Long> eventIds;
    String text;
    LocalDateTime start;
    LocalDateTime end;
    String sort;
}