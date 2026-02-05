package ru.yandex.practicum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

    Comment findCommentByIdAndPostId(Long commentId, Long postId);

    void deleteById(Long id);
}
