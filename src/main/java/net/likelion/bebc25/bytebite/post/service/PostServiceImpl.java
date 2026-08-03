package net.likelion.bebc25.bytebite.post.service;

import net.likelion.bebc25.bytebite.post.dto.PageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(@Qualifier("jdbcTemplatePostRepository") PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public PageDto<PostDto> getPosts(int page, int size, String sort) {
        int offset = (page - 1) * size;

        List<PostDto> posts;
        if(sort.equals("popular")){
             posts = postRepository.findPopular(offset, size);
        } else {
            posts = postRepository.findLatest(offset, size);
        }

        int totalCount = postRepository.countPost();
        return new PageDto<>(posts, page, size, totalCount);
    }

    @Override
    public int getPostCount(){
        return postRepository.countPost();
    }

    @Override
    public PostDto getPost(int id) {
        return postRepository.findById(id);
    }

    @Override
    public void increaseView(int postid){ postRepository.increaseView(postid);}

    @Override
    public void writePost(PostDto post) {
        postRepository.save(post);
    }

    @Override
    public void editPost(PostDto post) {
        postRepository.update(post);
    }

    @Override
    public void removePost(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto findByName(String keyword){
        return postRepository.findByName(keyword);
    }
}
