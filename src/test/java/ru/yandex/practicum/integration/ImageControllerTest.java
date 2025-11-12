package ru.yandex.practicum.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.configuration.DaoConfigurationTest;
import ru.yandex.practicum.configuration.IntegrationConfigurationTest;
import ru.yandex.practicum.controller.ImageController;
import ru.yandex.practicum.dto.PostDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitWebConfig(classes = {IntegrationConfigurationTest.class, DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class ImageControllerTest {
    @Autowired
    private ImageController imageController;

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private final ObjectMapper mapper = new ObjectMapper();
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    public void uploadImageTest() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
        MockMultipartFile file = new MockMultipartFile("image", "avatar.png", "image/png", pngStub);
//        String name = imageController.uploadFile(multipartFile, 1L);
//        assertEquals("file", name);
        mockMvc.perform(multipart("/posts/{id}/image", 1L).file(file)
                        .with(request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk());
    }

    @Test
    public void downloadImageTest() throws Exception {
        PostDto postDto = PostDto.builder().id(1L).title("title").text("text").commentsCount(0).likesCount(0).build();
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
        MockMultipartFile file = new MockMultipartFile("image", "avatar.png", "image/png", pngStub);
//        String name = imageController.uploadFile(multipartFile, 1L);
//        assertEquals("file", name);
        mockMvc.perform(multipart("/posts/{id}/image", 1L).file(file)
                        .with(request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{id}/image", 1L)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(pngStub));
//        ResponseEntity<Resource> resourceResponseEntity = imageController.downloadFile(1L);
//        assertNotNull(resourceResponseEntity);
    }


}
