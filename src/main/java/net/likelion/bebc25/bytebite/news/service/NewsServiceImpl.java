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
    public NewPageDto<PostDto> getNewsList(int page, int size, String sort, String category) {
        int validPage = page < 1 ? 1 : page;
        int validSize = size < 1 ? PAGE_SIZE : size;
        int offset = (validPage - 1) * validSize;

        List<PostDto> content;
        int totalCount;

        if (category == null || category.equals("all")) {
            content = "views".equals(sort)
                    ? postRepository.findAllNewsByViews(offset, validSize)
                    : postRepository.findAllNews(offset, validSize);
            totalCount = postRepository.countNews();
        } else {
            content = "views".equals(sort)
                    ? postRepository.findCategoryNewsByViews(category, offset, validSize)
                    : postRepository.findCategoryNews(category, offset, validSize);
            totalCount = postRepository.countCategoryNews(category);
        }

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

            Path directory = Paths.get(uploadDir, "news");
            Files.createDirectories(directory);
            imageFile.transferTo(directory.resolve(savedName));

            post.setImage("/uploads/news/" + savedName);
            postRepository.saveNews(post);

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }
    }

    @Override
    public NewPageDto<PostDto> searchNewsByKeyword(String keyword, int page, int size) {
        int validPage = page < 1 ? 1 : page;
        int validSize = size < 1 ? PAGE_SIZE : size;
        int offset = (validPage - 1) * validSize;

        List<PostDto> content = postRepository.findRestaurantNewsByKeyword(keyword, offset, validSize);
        int totalCount = postRepository.countRestaurantNews(keyword);

        return new NewPageDto<>(content, validPage, validSize, totalCount, PAGE_BLOCK_SIZE);
    }

    @Override
    public void editNews(PostDto post, SessionMemberDto loginMember) {
        PostDto origin = postRepository.findNewsById(post.getId());

        if (!"MANAGER".equals(loginMember.getRole()) || loginMember.getMemberId() != origin.getMemberId()) {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }

        MultipartFile[] images = post.getImages();

        if (images != null && images.length == 1 && !images[0].isEmpty()) {
            // 새로운 image 첨부했을때
            MultipartFile imageFile = images[0];
            try {
                String originalName = imageFile.getOriginalFilename();
                String extension = originalName.substring(originalName.lastIndexOf("."));
                String savedName = UUID.randomUUID() + extension;

                Path directory = Paths.get(uploadDir, "news");
                Files.createDirectories(directory);
                imageFile.transferTo(directory.resolve(savedName));

                post.setImage("/uploads/news/" + savedName);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장에 실패했습니다.", e);
            }
        } else {
            // 없으면 기존 이미지 경로 유지
            post.setImage(origin.getImage());
        }

        postRepository.updateNews(post);
    }

    @Override
    public void removeNews(int id, SessionMemberDto loginMember) {
        PostDto origin = postRepository.findNewsById(id);

        if (!"MANAGER".equals(loginMember.getRole()) || loginMember.getMemberId() != origin.getMemberId()) {
            throw new IllegalArgumentException("작성자 본인만 삭제할 수 있습니다.");
        }

        postRepository.deleteNewsById(id);
    }

    @Override
    public void increaseView(int id) {
        postRepository.increaseView(id);   // post와 완전히 동일한 메서드 재사용
    }
}