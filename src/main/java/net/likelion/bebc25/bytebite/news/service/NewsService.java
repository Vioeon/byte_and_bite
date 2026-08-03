package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;

public interface NewsService {
    List<PostDto> getNews();
    List<PostDto> getNewsByViews();

    PostDto getNews(int id); // get
    void writeNews(PostDto post); // post
//    void editNews(NewsDto post); // post
//    void removeNews(int id); // post
}