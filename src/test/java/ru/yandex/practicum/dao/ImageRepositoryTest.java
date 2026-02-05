package ru.yandex.practicum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.repository.ImageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJdbcTest
public class ImageRepositoryTest {
    @Autowired
    ImageRepository imageRepository;

    @Test
    void saveImageInfo() {
        Image image = new Image(null, "file", 1L);
        imageRepository.save(image);
        List<Image> imageList = imageRepository.findAllByPostId(1L);
        assertEquals(1, imageList.size());
    }

    @Test
    void deleteImageInfo() {
        Image image = new Image(null, "file", 1L);
        imageRepository.save(image);
        imageRepository.deleteByPostId(1L);
        List<Image> imageList = imageRepository.findAllByPostId(1L);
        assertEquals(0, imageList.size());
    }
}
