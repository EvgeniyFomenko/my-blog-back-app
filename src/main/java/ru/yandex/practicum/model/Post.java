package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "POST")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @Column
    private Long id;
    @Column
    private String title;
    @Column
    private String text;
    @Column
    private String tags;
    @Column
    private int likesCount;
    @Column
    private int commentsCount;
}
