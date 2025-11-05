package ru.yandex.practicum.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private List<PostDto> posts;
    private boolean hasPrev;
    private boolean hasNext;
    private Integer lastPage;
}
