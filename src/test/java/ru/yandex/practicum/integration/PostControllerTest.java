package ru.yandex.practicum.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.WebConfiguration;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitWebConfig(classes = {WebConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    public void setup() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
    public void getAllPosts() throws Exception {
        Post post2 = Post.builder().id(1L).text("text2").title("title2").tags("tags2").commentsCount(0).likesCount(0).build();
        postRepository.save(post2);

        mockMvc.perform(get("/posts?search=&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(2)))
                .andExpect(jsonPath("$.posts[1].title").value("title2"))
                .andExpect(jsonPath("$.posts[1].id").value(post2.getId()));
    }

    @Order(2)
    @Test
    public void getPostById() throws Exception {

        mockMvc.perform(get("/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(post.getId()));
    }

    @Order(3)
    @Test
    public void getPostLikesCount() throws Exception {
        mockMvc.perform(post("/posts/{id}/likes", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(PostMapper.toDto(post))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));
    }

    @Order(4)
    @Test
    public void getPostCommentsCount() throws Exception {
        CommentDto commentDto = new CommentDto("text", post.getId());

        mockMvc.perform(post("/posts/{id}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.postId").value(post.getId()));

        mockMvc.perform(get("/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.commentsCount").value(1));
//        PostDto postDto1 = postController.getPostById(1L);
//        assertEquals(1, postDto1.getCommentsCount());
    }

    @Order(5)
    @Test
    public void deletePost() throws Exception {

        mockMvc.perform(delete("/posts/{id}", post.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{id}", post.getId()))
                .andExpect(status().isNotFound());

    }

}
