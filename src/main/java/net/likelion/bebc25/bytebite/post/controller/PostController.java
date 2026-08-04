package net.likelion.bebc25.bytebite.post.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.post.dto.NewPageDto;
import net.likelion.bebc25.bytebite.post.dto.PageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.service.PostService;
import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;
import net.likelion.bebc25.bytebite.reply.service.ReplyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ReplyService replyService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public PostController(PostService postService, ReplyService replyService){
        this.postService = postService;
        this.replyService = replyService;
    }

    @GetMapping
    public String getPostList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "9") int size,
                               @RequestParam(defaultValue = "latest") String sort,
                               @RequestParam(defaultValue = "all") String category,
                               @RequestParam(required = false) String keyword,
                               Model model){

        PageDto<PostDto> pageDto;
        if(keyword != null && !keyword.isEmpty()){
            List<PostDto> searchList = postService.searchRestaurant(keyword);

            pageDto = new PageDto<>(searchList, page, size, searchList.size());
        } else {
            pageDto = postService.getPosts(page, size, sort, category);
        }

        int startPage = ((page - 1) / 5) * 5 + 1;
        int endPage = Math.min(startPage + 4, pageDto.getTotalPage());

        // 이미지 경로 추가
        for(PostDto post : pageDto.getContent()) {
            if(post.getImage() != null && !post.getImage().isEmpty()) {
                post.setImage("/uploads/posts/" + post.getImage());
            }
        }

        model.addAttribute("posts", pageDto.getContent());
        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", pageDto.getTotalPage());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("menu", "posts");
        return "post/postList";
    }

    @GetMapping("/{postId}")
    public String getDetail(@PathVariable int postId,
                            @RequestParam(defaultValue = "1") int page,
                            HttpSession session,
                            Model model){

        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        model.addAttribute(("loginMember"), loginMember);

        // 조회수 처리 - 중복되면 안돼서 HashSet
        Set<Integer> viewPostIds = (Set<Integer>) session.getAttribute("viewPostIds");

        // 조회 목록 세션 없으면 생성
        if(viewPostIds == null){
            viewPostIds = new HashSet<>();
            session.setAttribute("viewPostIds", viewPostIds);
        }

        // 처음 조회 시 조회수 1 증가
        if(!viewPostIds.contains(postId)){
            postService.increaseView(postId);
            viewPostIds.add(postId);
        }

        PostDto post = postService.getPost(postId);
        // DB에 저장된 이미지 문자열을 , 로 나누기
        // .split() - 문자열을 원하는 기준으로 나누어줌
        List<String> images = List.of(post.getImage().split(","));

        model.addAttribute("post", post);
        model.addAttribute("images", images);
        model.addAttribute("menu", "posts");

        // 댓글 조회 (페이지당 10개)
        NewPageDto<ReplyDto> replyPage =
                replyService.findByPostId(postId, page, 10);

        model.addAttribute("replyList", replyPage.getContent());

        // 전체 댓글 수 조회
        int totalReply = replyService.countByPostId(postId);
        model.addAttribute("replyCount", totalReply);

        // 페이지 정보 전달
        model.addAttribute("pageResponse", replyPage);

        return "post/detail"; // 템플릿 파일 경로
    }

    @GetMapping("/write")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post,
                               HttpSession session, Model model){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
       SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

       // 비회원이 리뷰 작성을 누르면 로그인 화면으로
       if(loginMember == null){
           return "redirect:/member/login";
       }
       model.addAttribute("menu", "posts");
       return "post/write";
    }

    // html에서 images 값을 가져오고, 여러 장의 파일을 가져오기 때문에 배열로 받음
    @PostMapping("/write")
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post,
                            BindingResult bindingResult,
                            @RequestParam(value="images", required = false)
                                MultipartFile[] images,
                            HttpSession session){
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");
        // 로그인 검사
        if(loginMember == null){
            return "redirect:/login";
        }

        // 식당 필수 입력 검사
        if(post.getRestaurantId() == 0){
            bindingResult.rejectValue("restaurantId", "required", "식당을 선택해주세요.");
        }

        // 이미지 첨부 필수 검사
        if(images == null || images.length == 0 || images[0].isEmpty()){
            bindingResult.rejectValue("images", "required",
                    "이미지는 필수 첨부 항목입니다.");
        }

        // 등록된 이미지 갯수 제한 검사
        if(images != null && images.length > 3){
            bindingResult.rejectValue("images", "max",
                    "이미지는 최대 3장까지 업로드 가능합니다.");
        }

        // 제목, 내용, 이미지 검증 실패 시 리턴
        if(bindingResult.hasErrors()){
            return "post/write";
        }

        post.setImages(images);
        postService.writePost(post, loginMember);
        return "redirect:/posts"; //
    }

    @GetMapping("/{postId}/edit")
    public String getEditForm(@PathVariable int postId, HttpSession session, Model model){
        SessionMemberDto loginMember = (SessionMemberDto)session.getAttribute("loginMember");
        // 로그인 확인
        if(loginMember == null){
            return "redirect:/login";
        }
        // 기존 게시글 조회
        PostDto post = postService.getPost(postId);
        // 작성자, 관리자만 수정 가능
        if(!loginMember.getRole().equals("MANAGER") &&  loginMember.getMemberId()!=post.getMemberId()){
            model.addAttribute("postForm", post);
            return "redirect:/posts";
        }
        model.addAttribute("postForm", post);
        // 수정 여부 구분
        model.addAttribute("edit",true);
        return "post/write";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult,
                           @RequestParam(value="images", required = false) MultipartFile[] images,
                           HttpSession session){
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");
        if(loginMember == null){
            return "redirect:/login";
        }

        // 기존 게시글 가져오기
        PostDto origin = postService.getPost(post.getId());
        if(images != null && images.length > 3){
            bindingResult.rejectValue("images", "max",
                    "이미지는 최대 3장까지 업로드 가능합니다.");
        }

        if(!loginMember.getRole().equals("MANAGER") && loginMember.getMemberId() != origin.getMemberId()){
            return "redirect:/posts";
        }

        if(bindingResult.hasErrors()){
            return "post/write";
        }

        postService.editPost(post, loginMember);
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable int postId,
                             HttpSession session){
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if(loginMember == null){
            return "redirect:/login";
        }

        PostDto post = postService.getPost(postId);

        if(!loginMember.getRole().equals("MANAGER") && loginMember.getMemberId() != post.getMemberId()){
            return "redirect:/login";
        }

        postService.removePost(postId, loginMember);
        return "redirect:/posts";
    }

    @GetMapping("/restaurant/search")
    @ResponseBody
    public PostDto search(@RequestParam String keyword) {
        PostDto post = postService.findByName(keyword);

        if (post == null) {
            return new PostDto();
        }
        return post;
    }
}