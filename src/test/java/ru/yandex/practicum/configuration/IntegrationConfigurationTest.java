package ru.yandex.practicum.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.controller.CommentController;
import ru.yandex.practicum.controller.ImageController;
import ru.yandex.practicum.controller.PostController;
import ru.yandex.practicum.repository.*;
import ru.yandex.practicum.service.*;

@Configuration
@ComponentScan("ru.yandex.practicum")
public class IntegrationConfigurationTest {

    @Bean
    @Primary
    public PostController postController(@Qualifier("postRepository")PostRepository postRepository) {
        PostService postService = new PostService(postRepository);
        return new PostController(postService);
    }


    @Bean
    @Primary
    public CommentController commentController(@Qualifier("postRepository")PostRepository postRepository, @Qualifier("commentRepository")CommentRepository commentRepository) {
        PostService postService = new PostService(postRepository);
        CommentService commentService = new CommentService(commentRepository, postService);

        return new CommentController(commentService, postService);
    }

    @Bean
    @Primary
    public FilesService filesService() {
        return new FilesServiceImpl();
    }

    @Bean
    @Primary
    public ImageController imageController(@Qualifier("imageRepository")ImageRepository imageRepository) {
        return new ImageController(new ImageService(filesService(), imageRepository));
    }
}
