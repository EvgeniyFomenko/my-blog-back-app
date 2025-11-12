package ru.yandex.practicum.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Image;

import java.util.List;

@Repository
@AllArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Image> findAllByPostId(Long postId) {
        return jdbcTemplate.query(
                "select id, name, post_id from image where post_id = ?",
                (rs, rowNum) -> new Image(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("post_id")
                ), postId);
    }

    @Override
    public void saveByPostId(Image image, Long postId) {
        System.out.println("save image by post id " + postId + " " + image.getName());
        jdbcTemplate.update("insert into image( name, post_id) values(?, ?)",
                image.getName(), postId);
    }

    @Override
    public void deleteByPostId(Long id) {
        jdbcTemplate.update("delete from image where post_id = ?", id);
    }

    @Override
    public void update(Long id, Image image) {
        jdbcTemplate.update("update image set name = ? where id = ?",
                image.getName(), id);
    }
}
