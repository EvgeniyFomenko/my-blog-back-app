package ru.yandex.practicum.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "IMAGE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Long postId;
}
