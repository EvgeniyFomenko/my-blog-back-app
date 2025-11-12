package ru.yandex.practicum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.configuration.DaoConfigurationTest;
import ru.yandex.practicum.configuration.ServiceConfigurationTest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(classes = {DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostRepositoryTest {
    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;

    @Test
    void findAll() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        List<Post> posts = postRepository.findAll();
        assertEquals(1, posts.size());
    }

    @Test
    void findByText() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        List<Post> posts = postRepository.findByText("text");
        assertEquals(1, posts.size());

    }

    @Test
    void save() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        Post postFind = postRepository.findById(1L);
        assertEquals(post, postFind);

    }

    @Test
    void deleteById() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        postRepository.deleteById(1L);
        assertThrows(NotFoundException.class, () -> postRepository.findById(1L));
    }

    @Test
    void update() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        post.setText("newText");
        postRepository.update(1L, post);
        assertEquals("newText", postRepository.findById(1L).getText());
    }

}
