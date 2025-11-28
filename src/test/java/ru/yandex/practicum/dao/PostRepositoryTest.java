package ru.yandex.practicum.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJdbcTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    public void setup() throws SQLException {
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        this.post = postRepository.save(post);
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
    void findAll(){
        List<Post> posts = (List<Post>) postRepository.findAll();
        assertEquals(1, posts.size());
    }

    @Order(2)
    @Test
    void findAllByTextLike()  {
        List<Post> posts = postRepository.findAllByTextLike("text");
        assertEquals(1, posts.size());

    }

    @Order(3)
    @Test
    void save() {
        Post postFind =  postRepository.findById(post.getId()).get();
        assertEquals(post, postFind);
    }

    @Order(5)
    @Test
    void deleteById() throws SQLException {
        postRepository.deleteById(post.getId());
        assertEquals(true, postRepository.findById(post.getId()).isEmpty());
    }

    @Order(4)
    @Test
    void update() {
        post.setText("newText");
        postRepository.save(post);
        assertEquals("newText", postRepository.findById(post.getId()).get().getText());
    }

}
