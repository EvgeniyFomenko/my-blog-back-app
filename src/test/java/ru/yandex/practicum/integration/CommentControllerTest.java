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
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitWebConfig(classes = {WebConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerTest {
    @Autowired
    private PostRepository postRepository;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    @Qualifier("commentRepository")
    private CommentRepository commentRepository;

    private Post post;
    private Comment comment;

    @BeforeEach
    public void setUp() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
    public void testAddComment() throws Exception {
        CommentDto commentDto = new CommentDto("text", post.getId());
//        commentController.saveComment(commentDto, 1L);

        mockMvc.perform(post("/posts/{id}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.postId").value(post.getId()));

    }

    @Order(2)
    @Test
    public void testGetComments() throws Exception {

        mockMvc.perform(get("/posts/{id}/comments", post.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text").value("comment"))
                .andExpect(jsonPath("$[0].postId").value(post.getId()))
                .andExpect(jsonPath("$[0].id").value(comment.getId()));
    }

    @Order(3)
    @Test
    public void testGetComment() throws Exception {

        mockMvc.perform(get("/posts/{id}/comments/{id}", post.getId(), comment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("comment"))
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.id").value(comment.getId()));
    }

    @Order(4)
    @Test
    public void testDeleteComment() throws Exception {

        mockMvc.perform(delete("/posts/{id}/comments/{id}", post.getId(), comment.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{id}/comments", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

    }

}
