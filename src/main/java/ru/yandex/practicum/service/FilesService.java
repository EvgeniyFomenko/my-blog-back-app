package ru.yandex.practicum.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String upload(MultipartFile file);

    Resource download(String nameFile);
}
