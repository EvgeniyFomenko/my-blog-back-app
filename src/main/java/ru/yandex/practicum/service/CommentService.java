package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public List<Comment> findAllByPostId(Long id) {
        return commentRepository.findAllByPostId(id);
    }

    public Comment findCommentByIdAndPostId(Long commentId,Long postId) {
        return commentRepository.findCommentByIdAndPostId(commentId,postId);
    }

    public void save(Comment comment, Long postId) {
        commentRepository.saveByPostId(comment, postId);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public void saveAndIncrementCountPostComments(Comment comment, Long postId) {
        commentRepository.saveByPostId(comment, postId);
        incrementCommentsCountPost(postId,1);
    }

    public void deleteAndDecrementCountPostComments(Long commentId, Long postId) {
        delete(commentId);
        incrementCommentsCountPost(postId,-1);
    }

    private void incrementCommentsCountPost(Long postId, int col) {
        Post post = postService.finById(postId);
        post.setCommentsCount(post.getCommentsCount() + col);
        postService.update(postId, post);
    }
}
