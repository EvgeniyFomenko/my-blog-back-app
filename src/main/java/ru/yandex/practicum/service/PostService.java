package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void save(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception ex) {
            System.out.println("Error saving post");
        }
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public void update(Long id, Post post) {
        postRepository.update(id, post);
    }

    public Post finById(Long id) {
        return postRepository.findById(id);
    }
}