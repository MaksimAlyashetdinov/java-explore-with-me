package ru.practicum.compilation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.NotFoundException;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventsService;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        Compilation compilation = Compilation.builder()
                                             .title(newCompilationDto.getTitle())
                                             .pinned(newCompilationDto.getPinned())
                                             .events(events)
                                             .build();
        Compilation savedCompilation = compilationRepository.saveAndFlush(compilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public List<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        compilations.addAll(compilationRepository.findAll(pageable).toList());
        return compilations;
    }

    @Override
    public Compilation getById(Integer compId) {
        return compilationRepository.findById(compId)
                                    .orElseThrow(() -> new NotFoundException(
                                 "Compilation with id " + compId + " not found."));
    }

    @Override
    public void deleteById(Integer compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public Compilation update(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getById(compId);
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents()
                  .isEmpty()) {
            compilation.getEvents()
                       .addAll(updateCompilationRequest.getEvents()
                                    .stream()
                                    .map(id -> eventRepository.findById(id).get())
                                    .collect(
                                            Collectors.toList()));
        }
        if (updateCompilationRequest.getPinned() != null) {
            updateCompilationRequest.getPinned();
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return compilationRepository.saveAndFlush(compilation);
    }
}