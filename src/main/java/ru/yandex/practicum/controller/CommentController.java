package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable("id") Long id) {
        return commentService.findAllByPostId(id);
    }

    @GetMapping("/{id}/comments/{idComment}")
    public Comment getComment(@PathVariable("id") Long id, @PathVariable("idComment") Long idComment) {
        return commentService.findCommentByIdAndPostId(idComment, id);
    }

    @PostMapping("/{id}/comments")
    public CommentDto saveComment(@RequestBody CommentDto comment, @PathVariable("id") Long id) {
        commentService.saveAndIncrementCountPostComments(new Comment(null, comment.getText(), id), id);
        return new CommentDto(comment.getText(), id);
    }

    @DeleteMapping("/{id}/comments/{idComment}")
    public void deleteComment(@PathVariable("id") Long id, @PathVariable("idComment") Long idComment) {
        commentService.deleteAndDecrementCountPostComments(idComment, id);
    }
}
