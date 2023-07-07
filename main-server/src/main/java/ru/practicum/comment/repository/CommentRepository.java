package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByInitiatorIdAndEventId(Long initiatorId, Long eventId);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.created between :start and :end")
    List<Comment> findByCommentIds(List<Long> commentIds, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.initiator.id IN :initiatorIds and " +
        "c.created between :start and :end")
    List<Comment> findByInitiatorIds(List<Long> initiatorIds, LocalDateTime start, LocalDateTime end,
                                        Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.event.id IN :eventIds and " +
        "c.created between :start and :end")
    List<Comment> findByEventIds(List<Long> eventIds, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where (c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findByText(String text, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.initiator.id IN :initiatorIds and " +
        "c.created between :start and :end")
    List<Comment> findByCommentAndInitiatorIds(List<Long> commentIds, List<Long> initiatorIds, LocalDateTime start,
                                                  LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.event.id IN :eventIds and " +
        "c.created between :start and :end")
    List<Comment> findByCommentAndEventIds(List<Long> commentIds, List<Long> eventIds, LocalDateTime start,
                                              LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findByCommentIdsAndText(List<Long> commentIds, String text, LocalDateTime start, LocalDateTime end,
                                             Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.initiator.id IN :initiatorIds and " +
        "c.event.id IN :eventIds and " +
        "c.created between :start and :end")
    List<Comment> findAllByInitiatorAndEventIds(List<Long> initiatorIds, List<Long> eventIds, LocalDateTime start,
                                                LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.initiator.id IN :initiatorIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByInitiatorIdsAndText(List<Long> initiatorIds, String text, LocalDateTime start,
                                               LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.event.id IN :eventIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByEventIdsAndText(List<Long> eventIds, String text, LocalDateTime start, LocalDateTime end,
                                           Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.initiator.id IN :initiatorIds and " +
        "c.event.id IN :eventIds and " +
        "c.created between :start and :end")
    List<Comment> findAllByCommentAndInitiatorAndEventIds(List<Long> commentIds, List<Long> initiatorIds,
                                                          List<Long> eventIds, LocalDateTime start, LocalDateTime end,
                                                          Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.initiator.id IN :initiatorIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByCommentAndInitiatorIdsAndText(List<Long> commentIds, List<Long> initiatorIds, String text,
                                                         LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.event.id IN :eventIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByCommentAndEventIdsAndText(List<Long> commentIds, List<Long> eventIds, String text,
                                                     LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.initiator.id IN :initiatorIds and " +
        "c.event.id IN :eventIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByInitiatorAndEventIdsAndText(List<Long> initiatorIds, List<Long> eventIds, String text,
                                                       LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT c from Comment c " +
        "where c.id IN :commentIds and " +
        "c.initiator.id IN :initiatorIds and " +
        "c.event.id IN :eventIds and " +
        "(c.text like %:text%) and " +
        "c.created between :start and :end")
    List<Comment> findAllByCommentAndInitiatorAndEventIdsAndText(List<Long> commentIds, List<Long> initiatorIds,
                                                                 List<Long> eventIds, String text, LocalDateTime start,
                                                                 LocalDateTime end, Pageable pageable);
}