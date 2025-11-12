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
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(classes = {IntegrationConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerTest {
    @Autowired
    private PostController postController;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
//        reset(wac);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    }

    @Test
    public void getAllPosts() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        PostDto postDto1 = PostDto.builder().id(2L).title("title2").text("text2").commentsCount(0).likesCount(0).build();
        postController.savePost(postDto);
        postController.savePost(postDto1);

        mockMvc.perform(get("/posts?search=&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(2)))
                .andExpect(jsonPath("$.posts[0].title").value("title"))
                .andExpect(jsonPath("$.posts[0].id").value(1));

//        PostResponseDto postResponseDto = postController.getPosts("", 1,5);
//        assertEquals(2,postResponseDto.getPosts().size());
    }

    @Test
    public void getPostById() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        postController.savePost(postDto);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(1));
//        PostDto post = postController.getPostById(1L);
//        assertNotNull(post);
    }

    @Test
    public void getPostLikesCount() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));

//        postController.savePost(postDto);
//        postController.likePost(1L);
//        PostDto post = postController.getPostById(1L);
//        assertEquals(1,post.getLikesCount());
    }

    @Test
    public void getPostCommentsCount() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        postController.savePost(postDto);
//        CommentDto commentDto = new CommentDto("text", 1L);
//        commentController.saveComment(commentDto,1L);
        CommentDto commentDto = new CommentDto("text", 1L);
//        commentController.saveComment(commentDto, 1L);

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.postId").value(1));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.commentsCount").value(1));
//        PostDto postDto1 = postController.getPostById(1L);
//        assertEquals(1, postDto1.getCommentsCount());
    }

    @Test
    public void deletePost() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        postController.savePost(postDto);

        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isNotFound());

    }

}
