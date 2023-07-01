package ru.practicum.compilation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation toCompilation(CompilationDto compilationDto) {
        return Compilation.builder()
                          .title(compilationDto.getTitle())
                          .pinned(compilationDto.getPinned())
                          .build();
    }


    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                             .id(compilation.getId())
                             .pinned(compilation.getPinned())
                             .title(compilation.getTitle())
                             .events(compilation.getEvents())
                             .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                          .title(newCompilationDto.getTitle())
                          .pinned(newCompilationDto.getPinned())
                          .build();
    }
}