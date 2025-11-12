package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select id, title, text,tags, likes_count, comments_count from post",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("tags"),
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count")
                ));
    }

    @Override
    public List<Post> findByText(String text) {
        return jdbcTemplate.query(
                "select id, title, text,tags, likes_count, comments_count from post where text like ?",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("tags"),
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count")
                ), "%"+text+"%");

    }

    @Override
    public Post findById(Long id) {
        List<Post> posts = jdbcTemplate.query("select id, title, text, tags, likes_count, comments_count  from post where id = ?", (rs, rowNum) -> new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                rs.getString("tags"),
                rs.getInt("likes_count"),
                rs.getInt("comments_count")
        ), id);

        if ( posts.size()>1) {
            throw new RuntimeException("Вернулось записей больше 1");
        }

        if (posts.isEmpty()) {
            throw new NotFoundException("Записи с id " + id + " не существует");
        }
        return  posts.get(0);
    }

    @Override
    public void save(Post post) throws SQLException {
        String query = "insert into post(title, text, tags, likes_count, comments_count) values(?, ?,?, ?, ?)";
        try(PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setString(3, post.getTags());
            preparedStatement.setInt(4, post.getLikesCount());
            preparedStatement.setInt(5, post.getCommentsCount());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        post.setId(id);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from post where id = ?", id);
    }

    @Override
    public void update(Long id, Post post) {
        jdbcTemplate.update("update post set title = ?, text = ?, likes_count = ?, comments_count = ?, tags = ? where id = ?",
                post.getTitle(), post.getText(), post.getLikesCount(), post.getCommentsCount(), post.getTags(), id);
    }

}