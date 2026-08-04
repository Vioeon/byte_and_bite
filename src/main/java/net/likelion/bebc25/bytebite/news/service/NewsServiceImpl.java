package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.post.dto.NewPageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class NewsServiceImpl implements NewsService {

    private final PostRepository postRepository;

    public NewsServiceImpl(@Qualifier("jdbcTemplatePostRepository") PostRepository postRepository){
        this.postRepository = postRepository;
    }

    private static final int PAGE_SIZE = 9;
    private static final int PAGE_BLOCK_SIZE = 5;

    @Override
    public NewPageDto<PostDto> getNewsList(int page, int size, String sort) {
        int validPage = page < 1 ? 1 : page;
        int validSize = size < 1 ? PAGE_SIZE : size;
        int offset = (validPage - 1) * validSize;

        List<PostDto> content;
        if ("views".equals(sort)) { // 조회수순 정렬
            content = postRepository.findAllNewsByViews(offset, validSize);
        } else { // 최신순 정렬 (default)
            content = postRepository.findAllNews(offset, validSize);
        }

        int totalCount = postRepository.countNews();
        return new NewPageDto<>(content, validPage, validSize, totalCount, PAGE_BLOCK_SIZE);
    }

    @Override
    public PostDto getNews(int id) {
        return postRepository.findNewsById(id);
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void writeNews(PostDto post, SessionMemberDto loginMember) {
        if (!"MANAGER".equals(loginMember.getRole())) {
            throw new IllegalArgumentException("매장 관리자만 소식을 작성할 수 있습니다.");
        }

        int restaurantId = postRepository.findRestaurantIdByMemberId(loginMember.getMemberId())
                .orElseThrow(() ->
                        new IllegalArgumentException("등록된 식당 정보가 없습니다."));

        MultipartFile[] images = post.getImages();

        if (images == null || images.length != 1 || images[0].isEmpty()) {
            throw new IllegalArgumentException("대표 이미지를 한 장 첨부해주세요.");
        }

        post.setMemberId(loginMember.getMemberId());
        post.setRestaurantId(restaurantId);
        post.setType("NEWS");

        MultipartFile imageFile = images[0];

        try {
            String originalName = imageFile.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String savedName = UUID.randomUUID() + extension;

            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);
            imageFile.transferTo(directory.resolve(savedName));

            post.setImage(savedName);
            postRepository.saveNews(post);

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }
    }

//    @Override
//    public void writeNews(PostDto posts) {
//        MultipartFile imageFile = posts.getImages()[0];
//        String savedFileName = fileStorageService.save(imageFile);
//        posts.setImage(savedFileName);
//        postRepository.saveNews(posts);
//    }
//
//    @Override
//    public void editNews(NewsDto posts) {
//        newsRepository.update(posts);
//    }
//
//    @Override
//    public void removeNews(int id) {
//        newsRepository.deleteById(id);
//    }
}