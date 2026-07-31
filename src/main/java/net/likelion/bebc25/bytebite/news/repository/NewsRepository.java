package net.likelion.bebc25.bytebite.news.repository;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;

import java.util.List;

public interface NewsRepository {
    List<NewsDto> findAll();
    List<NewsDto> findAllByViews();
//    NewsDto findById(int id);
//    void save(NewsDto post);
//    void update(NewsDto post);
//    void deleteById(int id);
}
