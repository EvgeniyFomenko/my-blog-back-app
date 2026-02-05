package ru.yandex.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@SpringBootTest(classes = {PostService.class, CommentService.class, PostRepository.class, CommentRepository.class})
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @MockitoBean
    private CommentRepository commentRepository;
    @MockitoBean
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
        Comment comment = new Comment(null, "comment", 1L);
        Mockito.doReturn(comment).when(commentRepository).save(comment);
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
        Comment comment = new Comment(null, "comment", 1L);
        Post post = new Post(null, "post", "text", null, 0, 1);
        Mockito.doReturn(Optional.of(post)).when(postRepository).findById(1L);
        Mockito.doReturn(comment).when(commentRepository).save(comment);
        commentService.saveAndIncrementCountPostComments(comment, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(comment);
        Mockito.verify(postRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteAndDecrementCountPostComments() {
        Post post = new Post(null, "post", "text", null, 0, 1);
        Mockito.doReturn(Optional.of(post)).when(postRepository).findById(1L);
        Mockito.doNothing().when(commentRepository).deleteById(1L);
        commentService.deleteAndDecrementCountPostComments(1L, 1L);
        Mockito.verify(postRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }
}