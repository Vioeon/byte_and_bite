package net.likelion.bebc25.bytebite.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/list.html")
    public String getBoardList(Model model){
        List<PostDto> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        // DB에 저장된 이미지 문자열을 , 로 나누기
        // .split() - 문자열을 원하는 기준으로 나누어줌
        List<String> images = List.of(post.getImage().split(","));

        model.addAttribute("post", post);
        model.addAttribute("images", images);
        return "post/detail"; // 템플릿 파일 경로
    }

    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
        return "post/write";
    }

    // html에서 images 값을 가져오고, 여러 장의 파일을 가져오기 때문에 배열로 받음
    @PostMapping("/write")
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post,
                            BindingResult bindingResult,
                            @RequestParam(value="images", required = false)
                                MultipartFile[] images){
        if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(error -> {
                log.info("필드 : {}, 오류 : {}",
                        error.getField(),
                        error.getDefaultMessage());
            });
            return "post/write"; //
        }

//         여러 장의 이미지를 등록 가능(MultipartFile[] 로 수정)
        if(images != null && images.length > 0){
            post.setImage(images[0].getOriginalFilename());
        }

        // 여러 이미지 이름을 하나의 문자열로 만들기 위한 객체
        StringBuilder sb = new StringBuilder();
        // 사용자가 선택한 이미지 개수만큼 반복
                for(int i = 0; i<images.length; i++){
                    // (업로드 파일.파일 원본 이름())
                    sb.append(images[i].getOriginalFilename());
                    // 파일 이름 사이 구분자 , 추가
                    if(i != images.length -1) {
                        sb.append(",");
                    }
                }
        // StringBuilder를 String으로 변환해서 DTO에 저장
                post.setImage(sb.toString());

        post.setMemberId(1); // 임시 회원 번호
        post.setRestaurantId(1); // 임시 식당 번호

        postService.writePost(post);
        return "redirect:/post/list.html"; //
    }

    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        model.addAttribute("postForm", post);
        return "post/write";
    }

    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "post/write";
        }

        postService.editPost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam int id){
        postService.removePost(id);
        return "redirect:/post/list.html";
    }
}