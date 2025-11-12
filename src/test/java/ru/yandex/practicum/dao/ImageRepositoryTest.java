package ru.yandex.practicum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.configuration.DaoConfigurationTest;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.repository.ImageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitWebConfig(classes = {DaoConfigurationTest.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class ImageRepositoryTest {
    @Autowired
            @Qualifier("imageRepository")
    ImageRepository imageRepository;

    @Test
    void saveImageInfo() {
        Image image = new Image(1L, "file", 1L);
        imageRepository.saveByPostId(image, 1L);
        List<Image> imageList = imageRepository.findAllByPostId(1l);
        assertEquals(1, imageList.size());
    }

    @Test
    void deleteImageInfo() {
        Image image = new Image(1L, "file", 1L);
        imageRepository.saveByPostId(image, 1L);
        imageRepository.deleteByPostId(1L);
        List<Image> imageList = imageRepository.findAllByPostId(1l);
        assertEquals(0, imageList.size());
    }
}
