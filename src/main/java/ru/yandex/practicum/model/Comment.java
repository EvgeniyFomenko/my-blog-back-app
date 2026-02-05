package ru.yandex.practicum.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "COMMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column
    private Long id;
    @Column
    private String text;
    @Column
    private Long postId;
}
