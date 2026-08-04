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
    public PageDto<PostDto> getPosts(int page, int size, String sort, String category) {
        // 페이지가 1보다 작으면 1 페이지로 처리
        int validPage = page < 1 ? 1 : page;
        // 한 페이지 게시글 수
        int validSize = size < 1 ? 9 : size;
        // 시작 위치 계산
        int offset = (validPage - 1) * validSize;

        List<PostDto> posts;
        int totalCount;
        if(category.equals("all")){
            if("views".equals(sort)){
                posts = postRepository.findViews(offset, validSize);
            } else {
                posts = postRepository.findLatest(offset, validSize);
            }
            totalCount = postRepository.countPost();
        } else {
            posts = postRepository.findByCategory(category, sort, offset, validSize);

            totalCount = postRepository.countCategory(category);
        }
        return new PageDto<>(posts, validPage, validSize, totalCount);

    }

    @Override
    public List<PostDto> searchRestaurant(String keyword){
        return postRepository.findByRestaurant(keyword);
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
