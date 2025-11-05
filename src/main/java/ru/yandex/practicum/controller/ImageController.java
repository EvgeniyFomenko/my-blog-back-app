package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.FilesService;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class ImageController {

    private final FilesService filesService;

    @PutMapping("/{id}/image")
    public String uploadFile(@RequestParam("image") MultipartFile file, @PathVariable("id") Long id) {
            return filesService.upload(file, id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id) {

        Resource file = filesService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}