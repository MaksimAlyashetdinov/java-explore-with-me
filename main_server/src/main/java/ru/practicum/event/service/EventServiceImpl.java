package ru.practicum.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.EventState;
import ru.practicum.event.EventStateAction;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final StatClient statClient;
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Event> getEventsByUserId(Integer userId, int from, int size) {
        containsUser(userId);
        Pageable pageable = PageRequest.of(from, size);
        log.info("Get events by user id {}", userId);
        return eventRepository.findByInitiatorId(userId, pageable)
                              .stream()
                              .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(Integer userId, NewEventDto newEventDto) {
        containsUser(userId);
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException(
                    "The date and time for which the event is scheduled cannot be earlier than two hours from the current moment");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory()).get();
        User initiator = userRepository.findById(userId).get();
        Event event = EventMapper.mapToEvent(newEventDto, category, initiator);
        event.setState(EventState.PENDING);
        Event savedEvent = eventRepository.saveAndFlush(event);
        log.info("Add event {}", newEventDto);
        return EventMapper.mapToEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEvent(Integer userId, Integer eventId) {
        containsUser(userId);
        containsEvent(eventId);
        log.info("Get event with id {}", eventId);
        return EventMapper.mapToEventFullDto(eventRepository.findById(eventId).get());
    }

    @Override
    public EventFullDto updateEvent(Integer userId, Integer eventId,
            UpdateEventUserRequest updateEventUserRequest) {
        containsUser(userId);
        containsEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!event.getState()
                  .equals(EventState.CANCELED) && !event.getState()
                                                        .equals(EventState.PENDING)) {
            throw new ConflictException(
                    "You can only change canceled events or events in the state of waiting for moderation.");
        }
        if (updateEventUserRequest.getStateAction() != null
                && updateEventUserRequest.getStateAction()
                                         .equals(EventStateAction.PUBLISH_EVENT)) {
            throw new ValidationException("You can't update publish event.");
        }
        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate()
                                                                                   .isBefore(
                                                                                           LocalDateTime.now()
                                                                                                        .plusHours(
                                                                                                                2))) {
            throw new ValidationException(
                    "The date and time for which the event is scheduled cannot be earlier than two hours from the current moment");
        }
        if (updateEventUserRequest.getAnnotation() != null
                && !updateEventUserRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryService.getCategory(updateEventUserRequest.getCategory()));
        }
        if (updateEventUserRequest.getDescription() != null
                && !updateEventUserRequest.getDescription().isBlank()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle()
                                                                                .isBlank()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction()
                                      .equals(EventStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            }
            if (updateEventUserRequest.getStateAction()
                                      .equals(EventStateAction.REJECT_EVENT)
                    || updateEventUserRequest.getStateAction()
                                             .equals(EventStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
            if (updateEventUserRequest.getStateAction()
                                      .equals(EventStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
        }
        eventRepository.saveAndFlush(event);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        log.info("Update event with id {}", eventId);
        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getFilteredEvents(List<Integer> users, List<String> states,
            List<Integer> categories, String rangeStart, String rangeEnd, Integer from,
            Integer size) {
        LocalDateTime start;
        LocalDateTime end;
        Pageable pageable = PageRequest.of(from, size);
        List<Event> result = new ArrayList<>();
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    FORMAT);
        }
        if (rangeEnd == null) {
            end = LocalDateTime.now().plusYears(1);
        } else {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    FORMAT);
        }
        if (users != null && states != null && categories != null) {
            List<EventState> statesForSearch = new ArrayList<>();
            for (String state : states) {
                EventState eventStateForSearch = EventState.valueOf(state);
                statesForSearch.add(eventStateForSearch);
            }
            result = eventRepository.searchEventsWithStates(users, statesForSearch, categories,
                    start, end, pageable);
        }
        if (users == null && states != null && categories != null) {
            List<EventState> statesForSearch = new ArrayList<>();
            for (String state : states) {
                EventState eventStateForSearch = EventState.valueOf(state);
                statesForSearch.add(eventStateForSearch);
            }
            result = eventRepository.searchEventsWithoutInitiator(statesForSearch, categories,
                    start, end, pageable);
        }
        if (users != null && states == null && categories != null) {
            result = eventRepository.searchEventsWithoutStates(users, categories, start, end,
                    pageable);
        }
        if (users != null && states != null && categories == null) {
            List<EventState> statesForSearch = new ArrayList<>();
            for (String state : states) {
                EventState eventStateForSearch = EventState.valueOf(state);
                statesForSearch.add(eventStateForSearch);
            }
            result = eventRepository.searchEventsWithoutCategory(users, statesForSearch, start, end,
                    pageable);
        }
        if (users == null && states == null && categories != null) {
            result = eventRepository.searchEventsWithoutInitiatorAndStates(categories, start, end,
                    pageable);
        }
        if (users == null && states != null && categories == null) {
            List<EventState> statesForSearch = new ArrayList<>();
            for (String state : states) {
                EventState eventStateForSearch = EventState.valueOf(state);
                statesForSearch.add(eventStateForSearch);
            }
            result = eventRepository.searchEventsWithoutInitiatorAndCategory(statesForSearch, start,
                    end, pageable);
        }
        if (users != null && states == null && categories == null) {
            result = eventRepository.searchEventsWithoutStatesAndCategory(users, start, end,
                    pageable);
        }
        if (users == null && states == null && categories == null) {
            result = eventRepository.searchEventsWithoutInitiatorAndStatesAndCategory(start, end,
                    pageable);
        }
        log.info("Get filtered events.");
        return result.stream()
                     .map(EventMapper::mapToEventFullDto)
                     .collect(Collectors.toList());
    }

    @Override
    public EventFullDto adminEventUpdate(Integer eventId,
            UpdateEventAdminRequest updateEventAdminRequest) {
        containsEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        Event updateEvent = eventUpdater(event, updateEventAdminRequest);
        Event savedEvent = eventRepository.saveAndFlush(updateEvent);
        log.info("Update event.");
        return EventMapper.mapToEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Integer> categories, Boolean paid,
            String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
            Integer size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from, size,
                Sort.by("EVENT_DATE".equals(sort) ? "eventDate" : "views"));
        LocalDateTime start;
        LocalDateTime end;
        List<Event> events = new ArrayList<>();
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    FORMAT);
        }
        if (rangeEnd == null) {
            end = LocalDateTime.now().plusYears(1);
        } else {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    FORMAT);
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Range end can't be before range start.");
        }
        if ((text == null || text.isBlank()) && (categories == null || categories.isEmpty())
                && paid == null) {
            events = eventRepository.findAllWithoutTextAndCategoryAndPaid(start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text == null || text.isBlank()) && (categories == null || categories.isEmpty())
                && paid != null) {
            events = eventRepository.findAllWithoutTextAndCategory(paid, start, end,
                    EventState.PUBLISHED, pageable);
        } else if ((text == null || text.isBlank()) && (categories != null && !categories.isEmpty())
                && paid == null) {
            events = eventRepository.findAllWithoutTextAndPaid(categories, start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text != null && !text.isBlank()) && (categories == null || categories.isEmpty())
                && paid == null) {
            events = eventRepository.findAllWithoutCategoryAndPaid(text, start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text != null && !text.isBlank()) && (categories != null
                && !categories.isEmpty()) && paid == null) {
            events = eventRepository.findAllWithFilterWithoutPaid(text, categories, start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text != null && !text.isBlank()) && (categories == null || categories.isEmpty())
                && paid != null) {
            events = eventRepository.findAllWithoutCategory(text, paid, start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text == null || text.isBlank()) && (categories != null && !categories.isEmpty())
                && paid != null) {
            events = eventRepository.findAllWithoutText(categories, paid, start,
                    end, EventState.PUBLISHED, pageable);
        } else if ((text != null && !text.isBlank()) && (categories != null
                && !categories.isEmpty()) && paid != null) {
            events = eventRepository.findAllWithFilter(text, categories, paid, start,
                    end, EventState.PUBLISHED, pageable);
        }
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                                                      .app("main_server")
                                                      .uri(request.getRequestURI())
                                                      .ip(request.getRemoteAddr())
                                                      .timestamp(LocalDateTime.now())
                                                      .build();
        statClient.addStats(endpointHitDto);
        log.info("Get events.");
        return events.stream()
                     .map(EventMapper::mapToEventShortDto)
                     .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getByIdWithCount(Integer id, HttpServletRequest request) {
        containsEvent(id);
        Event event = eventRepository.findById(id).get();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("There are no published events with this id.");
        }
        String start = event.getPublishedOn().format(FORMAT);
        String end = LocalDateTime.now().format(FORMAT);
        String[] uris = new String[1];
        uris[0] = request.getRequestURI();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStatsDto> viewStatsDtos = objectMapper
                .convertValue(statClient.getStats(start, end, uris, true)
                                        .getBody(), new TypeReference<>() {
                });
        Long sum = viewStatsDtos.stream()
                                .map(x -> x.getHits())
                                .reduce(0L, Long::sum);
        event.setViews(Long.valueOf(sum));
        eventRepository.saveAndFlush(event);
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                                                      .app("main_server")
                                                      .uri(request.getRequestURI())
                                                      .ip(request.getRemoteAddr())
                                                      .timestamp(LocalDateTime.now())
                                                      .build();
        statClient.addStats(endpointHitDto);
        log.info("Get event by id.");
        return EventMapper.mapToEventFullDto(event);
    }

    private void containsUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found.");
        }
    }

    private void containsEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id " + eventId + " not found.");
        }
    }

    private Event eventUpdater(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory())
                                                .orElseThrow(() -> new ValidationException(
                                                        "Category with id "
                                                                + updateEventAdminRequest.getCategory()
                                                                + " not found.")));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime newEventDate = updateEventAdminRequest.getEventDate();
            if (newEventDate.isAfter(event.getCreatedOn()
                                          .plusHours(1)) && event.getState()
                                                                 .equals(EventState.PENDING)) {
                event.setEventDate(newEventDate);
            } else {
                throw new ValidationException(
                        "The start date of the event must be no earlier than an hour from the date of publication.");
            }
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction()
                                          .equals(EventStateAction.CANCEL_REVIEW)) {
            if (event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new ValidationException(
                        "You can only cancel an event in the moderation waiting state.");
            }
        }
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction()
                                          .equals(EventStateAction.SEND_TO_REVIEW)) {
            event.setState(EventState.PENDING);
        }
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction()
                                          .equals(EventStateAction.PUBLISH_EVENT)) {
            EventState eventState = event.getState();
            if (eventState.equals(EventState.PUBLISHED)) {
                throw new ConflictException("The event has already been published.");
            }
            if (event.getState().equals(EventState.CANCELED)) {
                throw new ConflictException("The event has been canceled.");
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction()
                                          .equals(EventStateAction.REJECT_EVENT)) {
            if (event.getState()
                     .equals(EventState.PUBLISHED)) {
                throw new ConflictException("The event has already been published.");
            }
            event.setState(EventState.CANCELED);
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return event;
    }
}