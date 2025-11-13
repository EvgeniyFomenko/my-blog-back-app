package ru.yandex.practicum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.repository.*;

@Configuration
@ComponentScan("ru.yandex.practicum.configuration")
public class DaoConfigurationTest {


    @Bean("commentRepository")
    @Primary
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new CommentRepositoryImpl(jdbcTemplate);
    }

    @Bean("postRepository")
    @Primary
    public PostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new PostRepositoryImpl(jdbcTemplate);
    }

    @Bean("imageRepository")
    @Primary
    public ImageRepository imageRepository(JdbcTemplate jdbcTemplate) {
        return new ImageRepositoryImpl(jdbcTemplate);
    }
}
