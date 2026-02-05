package ru.yandex.practicum.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJdbcTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    private Post post;
    private Comment comment;

    @BeforeEach
    public void setup() {
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        this.post = postRepository.save(post);
//        this.post = post;


        Comment comment = new Comment(null, "comment", post.getId());
        this.comment = commentRepository.save(comment);
//        this.comment = commentRepository.findAllByPostId(post.getId()).get(0);
    }

    @AfterEach
    public void teardown() {
        List<Post> posts = (List<Post>) postRepository.findAll();
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
    void findCommentByIdAndPostId() {
        Comment commentFind = commentRepository.findCommentByIdAndPostId(comment.getId(), post.getId());
        assertEquals(comment, commentFind);
    }

    @Order(4)
    @Test
    void delete() {

        commentRepository.deleteById(comment.getId());
        assertEquals(null,  commentRepository.findCommentByIdAndPostId(comment.getId(), post.getId()));
    }


}
