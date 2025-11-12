package ru.yandex.practicum.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.configuration.ServiceConfigurationTest;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.repository.ImageRepository;
import ru.yandex.practicum.service.FilesService;
import ru.yandex.practicum.service.ImageService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {ServiceConfigurationTest.class} )
class ImageServiceTest {
    @Autowired
    @Qualifier("mockImageRepository")
    ImageRepository mockImageRepository;
    @Autowired
    FilesService filesServiceImpl;
    @Autowired
    @Qualifier("mockImageService")
    ImageService imageService;

    @Test
    void upload() {
        Image image = new Image(1L, "file", 1L);
        Image bySaved = new Image(null, "file", 1L);
        MultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        String file = "file";
        Mockito.doReturn(file).when(filesServiceImpl).upload(multipartFile);
        Mockito.doNothing().when(mockImageRepository).saveByPostId(image, 1L);
        Mockito.doNothing().when(mockImageRepository).deleteByPostId(1L);
        String fileReturn = imageService.uploadImage(multipartFile,1L);
        Mockito.verify(mockImageRepository, Mockito.atLeastOnce()).saveByPostId(bySaved, 1L);
        Mockito.verify(mockImageRepository, Mockito.atLeastOnce()).deleteByPostId(1L);
        assertEquals(file, fileReturn);
    }

    @Test
    void download() throws IOException {
        Image image = new Image(1L, "file", 1L);
        MultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        String file = "file";
        Mockito.doReturn(new ByteArrayResource(multipartFile.getBytes())).when(filesServiceImpl).download(file);
        Mockito.doReturn(List.of(image)).when(mockImageRepository).findAllByPostId(1L);
        Resource fileReturn = imageService.downloadImage(1L);
        assertNotNull(fileReturn);
    }
}