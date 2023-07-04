package ru.practicum.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.dto.NewComment;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment mapToComment(User initiator, Event event, NewComment newComment) {
        return Comment.builder()
            .initiator(initiator)
            .event(event)
            .text(newComment.getText())
            .created(LocalDateTime.now())
            .build();
    }
}