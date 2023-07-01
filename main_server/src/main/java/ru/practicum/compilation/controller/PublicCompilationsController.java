package ru.practicum.compilation.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class PublicCompilationsController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAllCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getAll(pinned, from, size)
                                 .stream()
                                 .map(CompilationMapper::toCompilationDto)
                                 .collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilations(@PathVariable Integer compId) {
        return CompilationMapper.toCompilationDto(compilationService.getById(compId));
    }
}