package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PostResponseDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public PostResponseDto findByText(String text, int pageSize, int pageNumber) {
        List<Post> posts = postRepository.findByText(text);
        int pages = getPages(pageSize, posts.size());
        posts = getSubList(pageSize, pageNumber, pages, posts);
        PostResponseDto postResponseDto = PostMapper.toResponseDto(posts);
        setPropertiesPostResponseDto(pageNumber, postResponseDto, pages);
        return postResponseDto;
    }

    /**
     * Устанавливаем параметры для дто которые требует клиент для отображения
     *
     * @param pageNumber      - номер страницы
     * @param postResponseDto - дто отдаваемая клиенту
     * @param pages           - количество страниц
     */
    private static void setPropertiesPostResponseDto(int pageNumber, PostResponseDto postResponseDto, int pages) {
        postResponseDto.setLastPage(pages);
        if (pageNumber > 1) {
            postResponseDto.setHasPrev(true);
        }
        if (pageNumber < pages) {
            postResponseDto.setHasNext(true);
        }
    }

    /**
     * Извлекаем из коллекции элементы требуемые для странницы
     *
     * @param pageSize   - размер страницы
     * @param pageNumber - номер страницы
     * @param pages      - количество страниц которые вмещает в себя коллекция
     * @param posts      - коллекция из которой извлекют
     * @return - возвращает коллекцию постов
     */
    private List<Post> getSubList(int pageSize, int pageNumber, int pages, List<Post> posts) {
        if (pages > 0 && pages >= pageNumber) {
            int start = pageSize * pageNumber - pageSize;
            int end = pages == pageNumber ? posts.size() : pageSize * pageNumber;
            posts = posts.subList(start, end);
        }
        return posts;
    }

    /**
     * Метод считает количество страниц
     *
     * @param pageSize - колличество элементов в коллекции
     * @param listSize - размер коллекции для которой производятся рассчёты
     * @return количечство страниц в коллекции
     */
    private int getPages(int pageSize, int listSize) {
        int pages = listSize / pageSize;
        if (listSize > pageSize && listSize % pageSize > 0) {
            pages++;
        }
        return pages;
    }


    public void save(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception ex) {
            System.out.println("Error saving post");
        }
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public void update(Long id, Post post) {
        postRepository.update(id, post);
    }

    public Post finById(Long id) {
        return postRepository.findById(id);
    }
}