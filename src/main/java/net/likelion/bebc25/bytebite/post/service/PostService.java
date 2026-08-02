package net.likelion.bebc25.bytebite.post.service;

import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getPosts();
    PostDto getPost(int id);
    void writePost(PostDto post);
    void editPost(PostDto post);
    void removePost(int id);

    void increaseView(int postid);
}
