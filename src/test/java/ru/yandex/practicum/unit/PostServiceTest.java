package ru.yandex.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.configuration.ServiceConfigurationTest;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@SpringJUnitWebConfig(classes = {ServiceConfigurationTest.class})
class PostServiceTest {
    @Autowired
    @Qualifier("mockCommentRepository")
    private CommentRepository commentRepository;
    @Autowired
    @Qualifier("mockPostRepository")
    private PostRepository postRepository;
    @Autowired
    @Qualifier("mockPostService")
    private PostService postService;

    @BeforeEach
    void setUp() {
        reset(commentRepository);
        reset(postRepository);
    }

    @Test
    void findAll() {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(Collections.singletonList(post)).when(postRepository).findAll();
        List<Post> posts = postService.findAll();
        assertEquals(1, posts.size());
    }

    @Test
    void findByText() {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(Collections.singletonList(post)).when(postRepository).findByText(post.getText());
        ru.yandex.practicum.dto.PostResponseDto postResponseDto = postService.findByText("text", 1, 1);
        List<PostDto> posts = postResponseDto.getPosts();
        assertEquals(1, posts.size());

    }

    @Test
    void save() throws SQLException {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doNothing().when(postRepository).save(post);
        postService.save(post);
        Mockito.verify(postRepository, Mockito.times(1)).save(post);

    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(postRepository).deleteById(1L);
        postService.deleteById(1L);
        Mockito.verify(postRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void update() {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doNothing().when(postRepository).update(1L, post);
        postService.update(1L, post);
        Mockito.verify(postRepository, Mockito.times(1)).update(1L, post);
    }

    @Test
    void finById() {
        Post post = Post.builder().id(1L).text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(post).when(postRepository).findById(1L);
        Post post1 = postService.finById(1L);
        assertEquals(post, post1);
    }
}