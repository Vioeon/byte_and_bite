package net.likelion.bebc25.bytebite.post.repository;

import net.likelion.bebc25.bytebite.post.dto.PostDto;

import java.util.List;

public interface PostRepository {
    List<PostDto> findAllPost();
    PostDto findById(int id);
    void save(PostDto post);
    void update(PostDto post);
    void deleteById(int id);

    void increaseView(int postid);
}
