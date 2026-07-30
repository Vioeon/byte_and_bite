package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> getNews();
//    NewsDto getNews(int id);
//    void writeNews(NewsDto post);
//    void editNews(NewsDto post);
//    void removeNews(int id);
}