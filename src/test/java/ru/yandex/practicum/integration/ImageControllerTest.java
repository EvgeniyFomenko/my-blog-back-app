package ru.yandex.practicum.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ImageControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    public void setup() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Post post = Post.builder().text("text").title("title").tags("tags").commentsCount(0).likesCount(0).build();
        postRepository.save(post);
        this.post = post;
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
    public void uploadImageTest() throws Exception {

        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
        MockMultipartFile file = new MockMultipartFile("image", "avatar.png", "image/png", pngStub);
        mockMvc.perform(multipart("/posts/{id}/image", post.getId()).file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    public void downloadImageTest() throws Exception {
        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
        MockMultipartFile file = new MockMultipartFile("image", "avatar.png", "image/png", pngStub);
        mockMvc.perform(multipart("/posts/{id}/image", post.getId()).file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{id}/image", post.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(pngStub));
    }


}
