package ru.yandex.practicum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.configuration.DaoConfigurationTest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(classes = {DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")

public class CommentRepositoryTest {
    @Autowired
    @Qualifier("commentRepository")
    private CommentRepository commentRepository;
    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;

    @Test
    void findAllByPostId() throws SQLException {
        Comment comment = new Comment(1L, "comment", 1L);
        Post post = new Post(1L, "title", "text", "tag tag1", 0, 0);
        postRepository.save(post);
        commentRepository.saveByPostId(comment, 1L);
        List<Comment> commentFind = commentRepository.findAllByPostId(1L);
        assertEquals(1, commentFind.size());
    }

    @Test
    void findCommentByIdAndPostId() throws SQLException {
        Comment comment = new Comment(1L, "comment", 1L);
        Post post = new Post(1L, "title", "text", "tag tag1", 0, 0);
        postRepository.save(post);
        commentRepository.saveByPostId(comment, 1L);
        Comment commentFind = commentRepository.findCommentByIdAndPostId(1L, 1L);
        assertEquals(comment, commentFind);
    }

    @Test
    void save() throws SQLException {
        Comment comment = new Comment(1L, "comment", 1L);
        Post post = new Post(1L, "title", "text", "tag tag1", 0, 0);
        postRepository.save(post);
        commentRepository.saveByPostId(comment, 1L);
        Comment commentFind = commentRepository.findCommentByIdAndPostId(1L, 1L);
        assertEquals(comment, commentFind);
    }

    @Test
    void delete() throws SQLException {
        Comment comment = new Comment(1L, "comment", 1L);
        Post post = new Post(1L, "title", "text", "tag tag1", 0, 0);
        postRepository.save(post);
        commentRepository.saveByPostId(comment, 1L);
        commentRepository.deleteById(1L);
        assertThrows(NotFoundException.class, () -> commentRepository.findCommentByIdAndPostId(1L, 1L));
    }


}
