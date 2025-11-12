package ru.yandex.practicum.configuration;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.repository.*;
import ru.yandex.practicum.service.*;

@Configuration
@ComponentScan("ru.yandex.practicum")
public class ServiceConfigurationTest {

    @Bean("mockCommentRepository")
    public CommentRepository commentRepository() {
        return Mockito.mock(CommentRepositoryImpl.class);
    }

    @Bean("mockPostRepository")
    public PostRepository postRepository() {
        return Mockito.mock(PostRepositoryImpl.class);
    }

    @Bean("mockImageRepository")
    public ImageRepository imageRepository() {
        return Mockito.mock(ImageRepositoryImpl.class);
    }

    @Bean("mockCommentService")
    public CommentService commentService(@Qualifier("mockCommentRepository")CommentRepository commentRepository, @Qualifier("mockPostService") PostService postService) {
        return new CommentService(commentRepository, postService);
    }

    @Bean("mockPostService")
    public PostService postService(@Qualifier("mockPostRepository")PostRepository postRepository) {
        return new PostService(postRepository);
    }

    @Bean("mockImageService")
    public ImageService imageService(@Qualifier("mockImageRepository")ImageRepository imageRepository, FilesService filesService) {
        return new ImageService(filesService, imageRepository);
    }


}
