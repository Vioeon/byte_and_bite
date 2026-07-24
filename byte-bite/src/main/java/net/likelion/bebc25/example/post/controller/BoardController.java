package net.likelion.bebc25.example.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.example.post.dto.PostDto;
import net.likelion.bebc25.example.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/homework/board")
public class BoardController {

    private final PostService postService;

    public BoardController(PostService postService){
        this.postService = postService;
    }

    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/list.html")
    public String getBoardList(Model model){
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        model.addAttribute("post", post);
        return "board/detail"; // 템플릿 파일 경로
    }

    // 게시글 등록 화면을 요청하는 컨트롤러
    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
        return "board/write";
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        model.addAttribute("postForm", post);
        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/write")
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post, // Validation 검증 대상 객체
                            BindingResult bindingResult){ // Validation 검증 결과 저장 객체(대상 객체 뒤에 기술해야 함)
        if(bindingResult.hasErrors()){ // 검증에 실패했을 경우
            return "board/write"; // 작성중이던 페이지로 다시 보낸다.
        }
        postService.writePost(post);
        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "board/write";
        }
        postService.editPost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    // 게시글 삭제 요청을 처리하는 컨트롤러
    @PostMapping("/delete")
    public String deletePost(@RequestParam int id){
        postService.removePost(id);
        return "redirect:list.html";
    }
}