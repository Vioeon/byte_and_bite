package net.likelion.bebc25.bytebite.post.service;

import net.likelion.bebc25.bytebite.post.dto.PageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;

public interface PostService {
    PageDto<PostDto> getPosts(int page, int size, String sort, String category);

    List<PostDto> searchRestaurant(String keyword);

    int getPostCount();

    PostDto getPost(int id);

    void writePost(PostDto post);

    void editPost(PostDto post);

    void removePost(int id);

    PostDto findByName(String keyword);

    void increaseView(int postid);

}
