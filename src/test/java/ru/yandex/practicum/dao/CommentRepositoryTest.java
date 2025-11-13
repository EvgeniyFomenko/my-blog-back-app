package ru.yandex.practicum.dao;

import org.junit.jupiter.api.*;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitWebConfig(classes = {DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")

public class CommentRepositoryTest {
    @Autowired
    @Qualifier("commentRepository")
    private CommentRepository commentRepository;
    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;

    private Post post;
    private Comment comment;

    @BeforeEach
    public void setup() throws SQLException {
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        this.post = post;


        Comment comment = new Comment(1L, "comment", post.getId());
        commentRepository.saveByPostId(comment, post.getId());
        this.comment = commentRepository.findAllByPostId(post.getId()).get(0);
    }

    @AfterEach
    public void teardown() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> {
            postRepository.deleteById(post.getId());
        });
    }


    @Order(1)
    @Test
    void findAllByPostId() {
        List<Comment> commentFind = commentRepository.findAllByPostId(post.getId());
        assertEquals(1, commentFind.size());
    }

    @Order(2)
    @Test
    void findCommentByIdAndPostId(){
        Comment commentFind = commentRepository.findCommentByIdAndPostId(comment.getId(), post.getId());
        assertEquals(comment, commentFind);
    }

    @Order(4)
    @Test
    void delete() {

        commentRepository.deleteById(comment.getId());
        assertThrows(NotFoundException.class, () -> commentRepository.findCommentByIdAndPostId(comment.getId(), post.getId()));
    }


}
