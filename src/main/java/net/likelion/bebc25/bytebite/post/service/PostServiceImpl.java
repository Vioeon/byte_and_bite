package net.likelion.bebc25.bytebite.post.service;

import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.post.dto.PageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

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
        PostDto post = postRepository.findById(id);

        if(post.getImage() != null && !post.getImage().isBlank()){
            String[] images = post.getImage().split(",");
            StringBuilder imagePath = new StringBuilder();

            for(int i = 0; i < images.length; i++){
                imagePath.append("/uploads/posts/").append(images[i]);

                if(i < images.length - 1){
                    imagePath.append(",");
                }
            }
            post.setImage(imagePath.toString());
        }
        if(post.getRestaurantImage() != null && !post.getRestaurantImage().isBlank()){
            // 식당 이미지 경로가 "/uploads/restaurant/"으로 시작하지 않는 경우
//                .startsWith("/uploads/restaurant/") 는 해당 문자열이 /uploads/restaurant/로 시작하는지 확인
            if(!post.getRestaurantImage().startsWith("/uploads/restaurant/")){
                // 이미지 파일명 앞에 "/uploads/restaurant/" 경로를 추가
                post.setRestaurantImage("/uploads/restaurant/" + post.getRestaurantImage());
            }
        }
        return post;
    }

    @Override
    public void increaseView(int postid){ postRepository.increaseView(postid);}

    @Override
    public void writePost(PostDto post, SessionMemberDto loginMember) {
        MultipartFile[] images = post.getImages();

        try {
            Path directory = Paths.get(uploadDir, "posts");
            Files.createDirectories(directory);

            StringBuilder imgUrl = new StringBuilder();
            for(int i = 0; i < images.length; i++){
                MultipartFile imageFile = images[i];

                String originalName = imageFile.getOriginalFilename();
                String extension = originalName.substring(originalName.lastIndexOf("."));
                String savedName = UUID.randomUUID() + extension;

                imageFile.transferTo(directory.resolve(savedName));
                imgUrl.append(savedName);

                if(i < images.length - 1){
                    imgUrl.append(",");
                }
            }
            post.setMemberId(loginMember.getMemberId());
            post.setType("POST");
            post.setImage(imgUrl.toString());

            postRepository.save(post);

        } catch (IOException e){
            throw new RuntimeException("이미지 저장에 실패했습니다." + e);
        }
    }

    @Override
    public void editPost(PostDto post, SessionMemberDto loginMember) {
        PostDto origin = postRepository.findById(post.getId());

        if(!"MANAGER".equals(loginMember.getRole()) && loginMember.getMemberId() != origin.getMemberId()){
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }

        MultipartFile[] images = post.getImages();

        if(images != null && images.length > 0 && !images[0].isEmpty()){
            try{
                // 저장 폴더 생성
                Path directory = Paths.get(uploadDir, "posts");
                Files.createDirectories(directory);

                // 기존 이미지 삭제
                if(origin.getImage() != null && !origin.getImage().isBlank()){
                    String[] oldImages = origin.getImage().split(",");

                    for(String oldImage : oldImages){
//                        String fileName = oldImage.replace("/uploads/posts/", "");
                        Files.deleteIfExists(directory.resolve(oldImage));
                    }
                }
                // 새 이미지 저장
                StringBuilder imgUrl = new StringBuilder();
                for(int i = 0; i < images.length; i++){
                    MultipartFile imageFile = images[i];

                    String originalName = imageFile.getOriginalFilename();
                    String extension = originalName.substring(originalName.lastIndexOf("."));
                    String savedName = UUID.randomUUID() + extension;

                    imageFile.transferTo(directory.resolve(savedName));
                    imgUrl.append(savedName);

                    if(i < images.length - 1){
                        imgUrl.append(",");
                    }
                }
                post.setImage(imgUrl.toString());

            } catch (IOException e){
                throw new RuntimeException("이미지 저장에 실패했습니다." + e);
            }
        } else {
            // 새 이미지를 선택하지 않으면 기존 이미지 유지
            post.setImage(origin.getImage());
        }
        postRepository.update(post);
    }

    @Override
    public void removePost(int postId, SessionMemberDto loginMember) {
        PostDto post = postRepository.findById(postId);

        try{
            if(post.getImage() != null && !post.getImage().isBlank()){
                Path directory = Paths.get(uploadDir, "posts");
                String[] images = post.getImage().split(",");

                for(String image : images){
//                    String fileName = image.replace(image);
                    Files.deleteIfExists(directory.resolve(image));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
        postRepository.deleteById(postId);
    }

    @Override
    public PostDto findByName(String keyword){
        return postRepository.findByName(keyword);
    }
}
