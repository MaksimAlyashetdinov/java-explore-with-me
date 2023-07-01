package ru.practicum.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ErrorResponse {
    LocalDateTime timestamp;
    Integer status;
    String error;
    String path;
}