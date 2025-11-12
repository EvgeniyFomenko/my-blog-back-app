package ru.yandex.practicum.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.configuration.IntegrationConfigurationTest;
import ru.yandex.practicum.controller.CommentController;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ActiveProfiles("test")
@SpringJUnitWebConfig(classes = {IntegrationConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerTest {
    @Autowired
    private CommentController commentController;
    @Autowired
    private PostController postController;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        postController.savePost(postDto);
    }

    @Test
    public void testAddComment() throws Exception {
        CommentDto commentDto = new CommentDto("text", 1L);
        commentController.saveComment(commentDto, 1L);

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.postId").value(1));

    }

    @Test
    public void testGetComments() throws Exception {
        CommentDto commentDto = new CommentDto("text", 1L);
        commentController.saveComment(commentDto, 1L);

        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text").value("text"))
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void testGetComment() throws Exception {
        CommentDto commentDto = new CommentDto("text", 1L);
        commentController.saveComment(commentDto, 1L);

        mockMvc.perform(get("/posts/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteComment() throws Exception {
        CommentDto commentDto = new CommentDto("text", 1L);
        commentController.saveComment(commentDto, 1L);
        commentController.deleteComment(1L, 1L);

        mockMvc.perform(delete("/posts/1/comments/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

    }

}
