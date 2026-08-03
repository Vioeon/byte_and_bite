package net.likelion.bebc25.bytebite.post.repository;

import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;

public interface PostRepository {
    List<PostDto> findAllPost(int offset, int pageSize);

    int countPost();

    List<PostDto> findLatest(int offset, int limit);

    List<PostDto> findPopular(int offset, int limit);

    PostDto findById(int id);

    void save(PostDto post);

    void update(PostDto post);

    void deleteById(int id);

    PostDto findByName(String keyword);

    void increaseView(int postId);

    // news
    List<PostDto> findAllNews();
    List<PostDto> findAllNewsByViews();
    PostDto findNewsById(int id);
    void saveNews(PostDto post);
    //    void update(NewsDto post);
    //    void deleteById(int id);
}