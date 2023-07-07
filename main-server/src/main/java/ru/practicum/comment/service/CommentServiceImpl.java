package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.NewComment;
import ru.practicum.comment.dto.UpdateComment;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.comment.util.Filter;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Comment addComment(NewComment newComment) {
        containsUser(newComment.getInitiatorId());
        containsEvent(newComment.getEventId());
        validateComment(newComment);
        User initiator = userRepository.findById(newComment.getInitiatorId()).get();
        Event event = eventRepository.findById(newComment.getEventId()).get();
        Comment comment = commentRepository.saveAndFlush(CommentMapper.mapToComment(initiator, event, newComment));
        log.info("Add comment {} from user {}.", newComment, newComment.getInitiatorId());
        return comment;
    }

    @Override
    public Comment getCommentById(Long userId, Long commentId) {
        containsUser(userId);
        containsComment(commentId);
        log.info("Get comment with id {}", commentId);
        return commentRepository.findById(commentId).get();
    }

    @Override
    public List<Comment> getComments(Filter filter, Pageable pageable) {
        LocalDateTime startDate = filter.getStart();
        LocalDateTime endDate = filter.getEnd();
        List<Long> commentsIds = filter.getCommentsIds();
        List<Long> userIds = filter.getUserIds();
        List<Long> eventIds = filter.getEventIds();
        String text = filter.getText();
        List<Comment> result = new ArrayList<>();
        if (filter.getStart() == null) {
            startDate = LocalDateTime.now().minusYears(100);
        }
        if (filter.getEnd() == null) {
            endDate = LocalDateTime.now().plusYears(100);
        }
        if (commentsIds == null && userIds == null && eventIds == null && text == null) {
            result = commentRepository.findAll(pageable).getContent();
        }
        if (commentsIds != null && userIds == null && eventIds == null && text == null) {
            result = commentRepository.findByCommentIds(commentsIds, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds != null && eventIds == null && text == null) {
            result = commentRepository.findByInitiatorIds(userIds, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds == null && eventIds != null && text == null) {
            result = commentRepository.findByEventIds(eventIds, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds == null && eventIds == null && text != null) {
            result = commentRepository.findByText(text, startDate, endDate, pageable);
        }
        if (commentsIds != null && userIds != null && eventIds == null && text == null) {
            result =
                commentRepository.findByCommentAndInitiatorIds(commentsIds, userIds, startDate, endDate, pageable);
        }
        if (commentsIds != null && userIds == null && eventIds != null && text == null) {
            result = commentRepository.findByCommentAndEventIds(commentsIds, eventIds, startDate, endDate, pageable);
        }
        if (commentsIds != null && userIds == null && eventIds == null && text != null) {
            result = commentRepository.findByCommentIdsAndText(commentsIds, text, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds != null && eventIds != null && text == null) {
            result = commentRepository.findAllByInitiatorAndEventIds(userIds, eventIds, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds != null && eventIds == null && text != null) {
            result = commentRepository.findAllByInitiatorIdsAndText(userIds, text, startDate, endDate, pageable);
        }
        if (commentsIds == null && userIds == null && eventIds != null && text != null) {
            result = commentRepository.findAllByEventIdsAndText(eventIds, text, startDate, endDate, pageable);
        }
        if (commentsIds != null && userIds != null && eventIds != null && text == null) {
            result =
                commentRepository.findAllByCommentAndInitiatorAndEventIds(commentsIds, userIds, eventIds, startDate,
                    endDate, pageable);
        }
        if (commentsIds != null && userIds != null && eventIds == null && text != null) {
            result =
                commentRepository.findAllByCommentAndInitiatorIdsAndText(commentsIds, userIds, text, startDate, endDate,
                    pageable);
        }
        if (commentsIds != null && userIds == null && eventIds != null && text != null) {
            result =
                commentRepository.findAllByCommentAndEventIdsAndText(commentsIds, eventIds, text, startDate, endDate,
                    pageable);
        }
        if (commentsIds == null && userIds != null && eventIds != null && text != null) {
            result = commentRepository.findAllByInitiatorAndEventIdsAndText(userIds, eventIds, text, startDate, endDate,
                pageable);
        }
        if (commentsIds != null && userIds != null && eventIds != null && text != null) {
            result =
                commentRepository.findAllByCommentAndInitiatorAndEventIdsAndText(commentsIds, userIds, eventIds, text,
                    startDate, endDate, pageable);
        }
        log.info("Get comments: " + result);
        return result;
    }

    @Override
    public Comment updateComment(UpdateComment updateComment) {
        containsUser(updateComment.getInitiatorId());
        containsComment(updateComment.getId());
        if (updateComment == null || updateComment.getText().isBlank()) {
            throw new ValidationException("Comment can't be empty.");
        }
        Comment comment = commentRepository.findById(updateComment.getId()).get();
        if (!comment.getInitiator().getId().equals(updateComment.getInitiatorId())) {
            throw new ConflictException("You can't update comment another user.");
        }
        comment.setText(updateComment.getText());
        log.info("Update comment with id " + comment.getId());
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        containsUser(userId);
        containsComment(commentId);
        Comment comment = commentRepository.getById(commentId);
        if (!comment.getInitiator().getId().equals(userId)) {
            throw new ConflictException("This user cannot delete a comment.");
        }
        log.info("Delete comment with id {}", commentId);
        commentRepository.delete(comment);
    }

    private void containsUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found.");
        }
    }

    private void containsEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id " + eventId + " not found.");
        }
    }

    private void containsComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with id " + commentId + " not found.");
        }
    }

    private void validateComment(NewComment newComment) {
        if (newComment == null || newComment.getText().isBlank()) {
            throw new ValidationException("Comment can't be empty.");
        }
    }
}