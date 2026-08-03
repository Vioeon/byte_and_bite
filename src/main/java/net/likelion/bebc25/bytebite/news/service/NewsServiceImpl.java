package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.post.dto.NewPageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final PostRepository postRepository;

    public NewsServiceImpl(@Qualifier("jdbcTemplatePostRepository") PostRepository postRepository){
        this.postRepository = postRepository;
    }

    private static final int PAGE_SIZE = 9;
    private static final int PAGE_BLOCK_SIZE = 5;

    @Override
    public NewPageDto<PostDto> getNewsList(int page, int size, String sort) {
        int validPage = page < 1 ? 1 : page;
        int validSize = size < 1 ? PAGE_SIZE : size;
        int offset = (validPage - 1) * validSize;

        List<PostDto> content;
        if ("views".equals(sort)) { // 조회수순 정렬
            content = postRepository.findAllNewsByViews(offset, validSize);
        } else { // 최신순 정렬 (default)
            content = postRepository.findAllNews(offset, validSize);
        }

        int totalCount = postRepository.countNews();
        return new NewPageDto<>(content, validPage, validSize, totalCount, PAGE_BLOCK_SIZE);
    }

    @Override
    public PostDto getNews(int id) {
        return postRepository.findNewsById(id);
    }

    @Override
    public void writeNews(PostDto post) {
        postRepository.save(post);
    }
//
//    @Override
//    public void editNews(NewsDto post) {
//        newsRepository.update(post);
//    }
//
//    @Override
//    public void removeNews(int id) {
//        newsRepository.deleteById(id);
//    }
}