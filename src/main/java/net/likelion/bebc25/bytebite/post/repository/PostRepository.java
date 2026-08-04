package net.likelion.bebc25.bytebite.post.repository;

import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<PostDto> findAllPost(int offset, int pageSize);

    int countPost();

    List<PostDto> findLatest(int offset, int limit);

    List<PostDto> findViews(int offset, int limit);

    List<PostDto> findByCategory(String category, String sort, int offset, int limit);

    List<PostDto> findByRestaurant(String keyword);

    int countCategory(String category);

    PostDto findById(int id);

    void save(PostDto post);

    void update(PostDto post);

    void deleteById(int id);

    PostDto findByName(String keyword);

    void increaseView(int postId);

    // news
    List<PostDto> findAllNews(int offset, int limit);
    List<PostDto> findAllNewsByViews(int offset, int limit);
    int countNews();
    PostDto findNewsById(int id);
    void saveNews(PostDto post);
    Optional<Integer> findRestaurantIdByMemberId(int memberId);
    //    void update(NewsDto post);
    //    void deleteById(int id);
}