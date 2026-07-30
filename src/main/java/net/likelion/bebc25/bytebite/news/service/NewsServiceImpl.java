package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import net.likelion.bebc25.bytebite.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(@Qualifier("jdbcTemplateNewsRepository") NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    @Override
    public List<NewsDto> getNews() {
        return newsRepository.findAll();
    }

//    @Override
//    public NewsDto getNews(int id) {
//        return newsRepository.findById(id);
//    }
//
//    @Override
//    public void writeNews(NewsDto post) {
//        newsRepository.save(post);
//    }
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