package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.NewComment;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/comments/user")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@PathVariable @NotNull @Positive Long userId,
                              @PathVariable @NotNull @Positive Long eventId,
                              @RequestBody @NotNull @Valid NewComment newComment) {
        log.info("Add comment request.");
        return commentService.addComment(userId, eventId, newComment);
    }

    @GetMapping("/{userId}/comment/{commentId}")
    public Comment getComment(@PathVariable @NotNull @Positive Long userId,
                              @PathVariable @NotNull @Positive Long commentId) {
        log.info("Get comment by id request.");
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping
    public List<Comment> getComments(
        @RequestParam(required = false) String text,
        @RequestParam(required = false) List<Long> comments,
        @RequestParam(required = false) List<Long> users,
        @RequestParam(required = false) List<Long> events,
        @RequestParam(required = false) LocalDateTime rangeStart,
        @RequestParam(required = false) LocalDateTime rangeEnd,
        @RequestParam(defaultValue = "created") String sort,
        @Min(0) @RequestParam(defaultValue = "0") Integer from,
        @Min(1) @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get comments request.");
        return commentService.getComments(comments, users, events, text, rangeStart, rangeEnd, sort, from, size);
    }

    @PatchMapping("/{userId}/comment/{commentId}")
    public Comment updateComment(@PathVariable @NotNull @Positive Long userId,
                                 @PathVariable @NotNull @Positive Long commentId,
                                 @RequestBody @NotNull @Valid NewComment newComment) {
        log.info("Update comment request.");
        return commentService.updateComment(userId, commentId, newComment);
    }

    @DeleteMapping("/{userId}/comment/{commentId}")
    public void getEventsByUserId(@PathVariable @NotNull @Positive Long userId,
                                  @PathVariable @NotNull @Positive Long commentId) {
        log.info("Delete comment request.");
        commentService.deleteComment(userId, commentId);
    }
}