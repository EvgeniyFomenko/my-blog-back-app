package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostResponseDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PostMapper {
    static public PostDto toDto(Post post) {
        List<String> tags = new ArrayList<>();
        if (Objects.nonNull(post.getTags())) {
           tags = Arrays.stream(post.getTags().split("\s")).toList();
        }

        return PostDto.builder()
                .commentsCount(post.getCommentsCount())
                .likesCount(post.getLikesCount())
                .tags(tags)
                .title(post.getTitle())
                .text(post.getText())
                .id(post.getId())
                .build();
    }

    static public Post toEntity(PostDto postDto) {
        return Post.builder().id(postDto.getId()).title(postDto.getTitle())
                .text(postDto.getText()).commentsCount(postDto.getCommentsCount()).likesCount(postDto.getLikesCount()).build();
    }

    static public PostResponseDto toResponseDto(List<Post> post) {
        return PostResponseDto.builder().posts(post.stream().map(PostMapper::toDto).toList()).hasPrev(false).hasNext(true).lastPage(1).build();
    }
}
