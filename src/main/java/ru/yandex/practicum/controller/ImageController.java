package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.ImageService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class ImageController {

    private final ImageService imageService;

    @PutMapping("/{id}/image")
    public String uploadFile(@RequestParam("image") MultipartFile file, @PathVariable("id") Long id) {
        return imageService.uploadImage(file, id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id) {
        Resource file = imageService.downloadImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}