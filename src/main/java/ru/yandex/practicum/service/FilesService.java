package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@Service
public class FilesService {
    private final ImageRepository imageRepository;
    public static final String UPLOAD_DIR = "uploads/";

    public String upload(MultipartFile file, long id) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Сохраняем файл
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            file.transferTo(filePath);
            imageRepository.deleteByPostId(id);
            imageRepository.saveByPostId(new Image(null, file.getOriginalFilename(),id),id);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Resource download(Long id) {
        List<Image> imageList =  imageRepository.findAllByPostId(id);
        Image image;
        if(imageList.isEmpty()){
            throw new RuntimeException("Изображение не найдено или отсутсвует");
        }

        image = imageList.get(0);

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(image.getName()).normalize();
            byte[] content = Files.readAllBytes(filePath);

            return new ByteArrayResource(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}