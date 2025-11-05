package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAllByPostId(Long postId);

    void saveByPostId(Comment comment, Long postId);

    Comment findCommentByIdAndPostId(Long commentId,Long postId);

    void deleteById(Long id);

    void update(Long id, Comment post);
}
