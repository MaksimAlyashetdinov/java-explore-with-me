package ru.practicum.compilation.dto;

import java.util.List;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {

    List<Integer> events;
    Boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}