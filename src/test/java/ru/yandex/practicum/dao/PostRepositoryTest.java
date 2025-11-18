package ru.yandex.practicum.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.configuration.DaoConfigurationTest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitWebConfig(classes = {DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostRepositoryTest {
    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    public void setup() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        this.post = post;
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
    void findAll(){
        List<Post> posts = postRepository.findAll();
        assertEquals(1, posts.size());
    }

    @Order(2)
    @Test
    void findByText()  {
        List<Post> posts = postRepository.findByText("text");
        assertEquals(1, posts.size());

    }

    @Order(3)
    @Test
    void save() {
        Post postFind = postRepository.findById(post.getId());
        assertEquals(post, postFind);
    }

    @Order(5)
    @Test
    void deleteById() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        postRepository.deleteById(1L);
        assertThrows(NotFoundException.class, () -> postRepository.findById(1L));
    }

    @Order(4)
    @Test
    void update() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        post.setText("newText");
        postRepository.update(post.getId(), post);
        assertEquals("newText", postRepository.findById(post.getId()).getText());
    }

}
