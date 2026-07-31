package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> getNews();
    List<NewsDto> getNewsByViews();

//    NewsDto getNews(int id); // get
//    void writeNews(NewsDto post); // post
//    void editNews(NewsDto post); // post
//    void removeNews(int id); // post
}