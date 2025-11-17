package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.repository.ImageRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ImageService {
    private final FilesService filesService;
    private final ImageRepository imageRepository;
    @Transactional
    public String uploadImage(MultipartFile file, long id) {
        String fileName = filesService.upload(file);
        imageRepository.deleteByPostId(id);
        imageRepository.saveByPostId(new Image(null, fileName, id), id);
        return fileName;
    }

    public Resource downloadImage(Long id) {
        Image image;
        List<Image> imageList = imageRepository.findAllByPostId(id);
        if (imageList.isEmpty()) {
            throw new NotFoundException("Изображение не найдено или отсутсвует");
        }
        image = imageList.get(0);
        return filesService.download(image.getName());
    }
}
