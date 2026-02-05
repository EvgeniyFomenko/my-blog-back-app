package ru.yandex.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@SpringBootTest(classes = {PostService.class, PostRepository.class, CommentRepository.class})
class PostServiceTest {
    @MockitoBean
    private CommentRepository commentRepository;
    @MockitoBean
    private PostRepository postRepository;
    @Autowired
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
        Mockito.doReturn(Collections.singletonList(post)).when(postRepository).findAllByTextLike(post.getText());
        ru.yandex.practicum.dto.PostResponseDto postResponseDto = postService.findByText("text", 1, 1);
        List<PostDto> posts = postResponseDto.getPosts();
        assertEquals(1, posts.size());

    }

    @Test
    void save() throws SQLException {
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(post).when(postRepository).save(post);
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
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(post).when(postRepository).save(post);
        postService.update(1L, post);
        Mockito.verify(postRepository, Mockito.times(1)).save(post);
    }

    @Test
    void finById() {
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        Mockito.doReturn(Optional.of(post)).when(postRepository).findById(1L);
        Post post1 = postService.finById(1L);
        assertEquals(post, post1);
    }
}