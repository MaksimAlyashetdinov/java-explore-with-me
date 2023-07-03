package ru.practicum.event.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventState;
import ru.practicum.user.model.User;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Size(min = 20, max = 2000)
    @Column(name = "annotation")
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Size(min = 20, max = 7000)
    @Column(name = "description")
    String description;
    @Column(name = "event_Date")
    LocalDateTime eventDate;
    @Column(name = "lat")
    Float lat;
    @Column(name = "lon")
    Float lon;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "participant_Limit")
    int participantLimit;
    @Column(name = "request_Moderation")
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    @Column(name = "title")
    String title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Column(name = "created_On")
    LocalDateTime createdOn;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventState state;
    @Column(name = "confirmed_Requests")
    int confirmedRequests;
    @Column(name = "published_On")
    LocalDateTime publishedOn;
    @Column(name = "views")
    Long views;
}