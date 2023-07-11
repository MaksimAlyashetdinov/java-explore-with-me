package ru.practicum.comment.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.comment.dto.NewComment;
import ru.practicum.comment.dto.UpdateComment;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.util.CommentFilter;

import java.util.List;

public interface CommentService {

    Comment addComment(NewComment newComment);

    Comment getCommentById(Long userId, Long commentId);

    List<Comment> getComments(CommentFilter filter, Pageable pageable);

    Comment updateComment(UpdateComment updateComment);

    void deleteComment(Long userId, Long commentId);
}