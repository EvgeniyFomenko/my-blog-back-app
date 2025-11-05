package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostResponseDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public PostResponseDto getPosts(@RequestParam("search") String search, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        return postService.findByText(search, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") Long id) {
        Post post = postService.finById(id);
        return PostMapper.toDto(postService.finById(id));
    }

    @PostMapping
    public PostDto savePost(@RequestBody PostDto post) {
        Post postEntity = PostMapper.toEntity(post);
        System.out.println(postEntity.toString());
        postService.save(postEntity);
        return PostMapper.toDto(postEntity);
    }

    @PutMapping("/{id}")
    public PostDto updatePost(@RequestBody PostDto post, @PathVariable("id") Long id) {
        Post postEntity = PostMapper.toEntity(post);
        postService.update(id, postEntity);
        return PostMapper.toDto(postEntity);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        postService.deleteById(id);
    }

    @PostMapping("/{id}/likes")
    public int likePost(@PathVariable("id") Long id) {
        Post post = postService.finById(id);
        post.setLikesCount(post.getLikesCount() + 1);
        postService.update(id, post);
        return post.getLikesCount();
    }
}
