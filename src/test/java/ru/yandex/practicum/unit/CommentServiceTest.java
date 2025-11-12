package ru.yandex.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.configuration.ServiceConfigurationTest;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@SpringJUnitWebConfig(classes = {ServiceConfigurationTest.class})
class CommentServiceTest {
    @Autowired
    @Qualifier("mockCommentService")
    private CommentService commentService;

    @Autowired
    @Qualifier("mockCommentRepository")
    private CommentRepository commentRepository;
    @Autowired
    @Qualifier("mockPostRepository")
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        reset(commentRepository);
    }

    @Test
    void findAllByPostId() {
        Comment comment = new Comment(1L, "comment", 1L);
        Mockito.doReturn(List.of(comment)).when(commentRepository).findAllByPostId(1L);
        List<Comment> commentFind = commentService.findAllByPostId(1L);
        assertEquals(1, commentFind.size());
    }

    @Test
    void findCommentByIdAndPostId() {
        Comment comment = new Comment(1L, "comment", 1L);
        Mockito.doReturn(comment).when(commentRepository).findCommentByIdAndPostId(1L, 1L);
        Comment commentFind = commentService.findCommentByIdAndPostId(1L, 1L);
        assertEquals(comment, commentFind);
    }

    @Test
    void save() {
        Comment comment = new Comment(1L, "comment", 1L);
        Mockito.doNothing().when(commentRepository).saveByPostId(comment, 1L);
        commentService.save(comment, 1L);
    }

    @Test
    void delete() {
        Mockito.doNothing().when(commentRepository).deleteById(1L);
        commentService.delete(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void saveAndIncrementCountPostComments() {
        Comment comment = new Comment(1L, "comment", 1L);
        Post post = new Post(1L, "post", "text", null, 0, 1);
        Mockito.doReturn(post).when(postRepository).findById(1L);
        Mockito.doNothing().when(commentRepository).saveByPostId(comment, 1L);
        commentService.saveAndIncrementCountPostComments(comment, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).saveByPostId(comment, 1L);
        Mockito.verify(postRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteAndDecrementCountPostComments() {
        Post post = new Post(1L, "post", "text", null, 0, 1);
        Mockito.doReturn(post).when(postRepository).findById(1L);
        Mockito.doNothing().when(commentRepository).deleteById(1L);
        commentService.deleteAndDecrementCountPostComments(1L, 1L);
        Mockito.verify(postRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }
}