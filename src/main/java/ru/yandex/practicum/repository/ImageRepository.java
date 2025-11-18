package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Image;

import java.util.List;

public interface ImageRepository {
    List<Image> findAllByPostId(Long postId);

    void saveByPostId(Image image, Long postId);

    void deleteByPostId(Long id);

    void update(Long id, Image post);
}
