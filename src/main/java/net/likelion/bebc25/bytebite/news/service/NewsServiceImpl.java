package net.likelion.bebc25.bytebite.post.service;

//import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
//import net.likelion.bebc25.bytebite.news.repository.NewsRepository;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final PostRepository postRepository;

    public NewsServiceImpl(@Qualifier("jdbcTemplatePostRepository") PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getNews() {
        return postRepository.findAllNews();
    }

    public List<PostDto> getNewsByViews() {
        return postRepository.findAllNewsByViews();
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