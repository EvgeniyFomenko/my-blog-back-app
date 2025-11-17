package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.exception.ServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
@Service
public class FilesServiceImpl implements FilesService {
    public static final String UPLOAD_DIR = "uploads/";

    @Override
    @Transactional
    public String upload(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Сохраняем файл
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            file.transferTo(filePath);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }

    @Override
    public Resource download(String nameFile) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(nameFile).normalize();
            byte[] content = Files.readAllBytes(filePath);

            return new ByteArrayResource(content);
        } catch (IOException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }

}