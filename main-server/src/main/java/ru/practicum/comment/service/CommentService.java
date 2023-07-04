package ru.practicum.comment.service;

import ru.practicum.comment.dto.NewComment;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Comment addComment(Long userId, Long eventId, NewComment newComment);

    Comment getCommentById(Long userId, Long commentId);

    List<Comment> getComments(List<Long> commentsIds, List<Long> userIds, List<Long> eventIds, String text,
                              LocalDateTime start, LocalDateTime end, String sort, int from, int size);

    Comment updateComment(Long userId, Long commentId, NewComment newComment);

    void deleteComment(Long userId, Long commentId);
}