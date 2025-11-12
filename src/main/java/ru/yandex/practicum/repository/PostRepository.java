package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;

import java.sql.SQLException;
import java.util.List;

public interface PostRepository {
    List<Post> findAll();

    List<Post> findByText(String text);

    void save(Post post) throws SQLException;

    void deleteById(Long id);

    void update(Long id, Post post);

    Post findById(Long id);
}