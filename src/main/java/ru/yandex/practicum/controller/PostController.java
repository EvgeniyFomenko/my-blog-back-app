package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostResponseDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public PostResponseDto getPosts(@RequestParam("search") String search, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        System.out.println("Search: " + search + ", pageNumber: " + pageNumber + ", pageSize: " + pageSize);
        return PostMapper.toResponseDto(postService.findAll());
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") Long id) {
        Post post = postService.finById(id);
        System.out.println("Post: " + post.toString());
        return PostMapper.toDto(postService.finById(id));
    }

    @PostMapping
    public PostDto savePost(@RequestBody PostDto post) {
        Post postEntity = new Post();
        postEntity.setText(post.getText());
        postEntity.setTitle(post.getTitle());
        String tags = "";
        if (Objects.nonNull(post.getTags())) {
            tags = String.join(" ", post
                    .getTags());
        }
        postEntity.setTags(tags);
        System.out.println(postEntity.toString());
        postService.save(postEntity);
        return PostMapper.toDto(postEntity);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        postService.deleteById(id);
    }
}
