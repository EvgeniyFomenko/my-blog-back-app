package ru.yandex.practicum.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.util.List;
@AllArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return jdbcTemplate.query(
                "select id, text, post_id from comment where post_id = ?",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("post_id")
                ),postId);
    }

    @Override
    public Comment findCommentByIdAndPostId(Long commentId,Long postId) {
        List<Comment> comments = jdbcTemplate.query(
                "select id, text, post_id from comment where post_id = ? and id = ?",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("post_id")
                ),postId, commentId);
        if(comments.size()>0){
            return comments.get(0);
        }
        throw new RuntimeException("Коментария с id " + commentId + " несуществует");
    }

    @Override
    public void saveByPostId(Comment comment, Long postId) {
        jdbcTemplate.update("insert into comment( text, post_id) values(?, ?)",
               comment.getText(), postId);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from comment where id = ?", id);
    }

    @Override
    public void update(Long id, Comment post) {
        jdbcTemplate.update("update comment set text = ? where id = ?",
                post.getText(), id);
    }
}
