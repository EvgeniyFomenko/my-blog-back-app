package ru.yandex.practicum.configuration;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.repository.*;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.FilesService;
import ru.yandex.practicum.service.ImageService;
import ru.yandex.practicum.service.PostService;

@Configuration
//@ComponentScan("ru.yandex.practicum")
@Profile("test")
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
    public CommentService commentService(@Qualifier("mockCommentRepository") CommentRepository commentRepository, @Qualifier("mockPostService") PostService postService) {
        return new CommentService(commentRepository, postService);
    }

    @Bean("mockPostService")
    public PostService postService(@Qualifier("mockPostRepository") PostRepository postRepository) {
        return new PostService(postRepository);
    }

    @Bean
    public FilesService filesService() {
        return Mockito.mock(FilesService.class);
    }

    @Bean("mockImageService")
    public ImageService imageService(@Qualifier("mockImageRepository") ImageRepository imageRepository, FilesService filesService) {
        return new ImageService(filesService, imageRepository);
    }
}
