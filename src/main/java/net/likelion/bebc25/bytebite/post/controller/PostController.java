package net.likelion.bebc25.bytebite.post.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.post.dto.PageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.service.PostService;
import net.likelion.bebc25.bytebite.reply.service.ReplyService;
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

    public PostController(PostService postService, ReplyService replyService){
        this.postService = postService;
        this.replyService = replyService;
    }

    @GetMapping
    public String getBoardList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "latest") String sort,
                               Model model){
        int size = 9;

        PageDto<PostDto> pageDto = postService.getPosts(page, size, sort);


        model.addAttribute("pageDto", pageDto);
        model.addAttribute("posts", pageDto.getContent());
        model.addAttribute("sort", sort);
        model.addAttribute("menu", "posts");
        return "post/postList";
    }

    @GetMapping("/{postId}")
    public String getDetail(@PathVariable int postId,
                            @RequestParam(defaultValue = "1") int page,
                            HttpSession session,
                            Model model){
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
        model.addAttribute("replyList", replyService.findByPostId(postId, page));

        // 전체 댓글 수 조회
        int totalReply = replyService.countByPostId(postId);

        // 화면으로 전달
        model.addAttribute("replyCount", totalReply);

        // 전체 페이지 수 계산
        int totalPage = (int) Math.ceil((double) totalReply / 10);

        // 화면으로 전달
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);

        return "post/detailTest"; // 템플릿 파일 경로
    }

    @GetMapping("/write")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post,
                               HttpSession session){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
       SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

       // 비회원이 리뷰 작성을 누르면 로그인 화면으로
       if(loginMember == null){
           return "redirect:/member/login";
       }
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
        // 이미지 첨부 필수 검사
        if(images == null || images.length == 0 || images[0].isEmpty()){
            bindingResult.rejectValue("images", "required",
                    "이미지는 필수 첨부 항목입니다.");
        }

        // 로그인 검사
        if(loginMember == null){
            return "redirect:/login";
        }

        // 제목, 내용, 이미지 검증 실패 시 리턴
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(System.out::println);
            return "post/write";
        }

        // 이미지 저장 경로
        String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
        File uploadDir = new File(uploadPath);

        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        // DB 저장용 url 생성
        StringBuilder imageUrl = new StringBuilder();

        for(int i = 0; i < images.length; i++){
            MultipartFile image = images[i];
            if(!image.isEmpty()){
                String fileName = java.util.UUID.randomUUID() + "_" + image.getOriginalFilename();
                File saveFile = new File(uploadPath + fileName);
                try {
                    // 실제 파일 저장
                    image.transferTo(saveFile);

                } catch (IOException e){
                    throw new RuntimeException(e);
                }

                // DB 저장 url
                imageUrl.append("/uploads/").append(image.getOriginalFilename());
                // 여러 이미지 구분
                if(i < images.length -1) {
                    imageUrl.append(",");
                }
            }
        }

        post.setMemberId(loginMember.getMemberId());
        post.setRestaurantId(1); // 임시 식당 번호
        post.setType("POST");
        post.setImage(imageUrl.toString()); // DB에 url 저장

        postService.writePost(post);
        return "redirect:/posts"; //
    }

    @GetMapping("/edit")
    public String getEditForm(@RequestParam("id") int id, HttpSession session, Model model){
        SessionMemberDto loginMember = (SessionMemberDto)session.getAttribute("loginMember");
        if(loginMember == null){
            return "redirect:/login";
        }

        PostDto post = postService.getPost(id);

        if(!loginMember.getRole().equals("MANAGER") &&  loginMember.getMemberId()!=post.getMemberId()){
            model.addAttribute("postForm", post);
            return "/posts";
        }
        model.addAttribute("postForm", post);
        return "post/write";
    }

    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult,
                           HttpSession session){
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");
        if(loginMember == null){
            return "redirect:/login";
        }
        PostDto origin = postService.getPost(post.getId());

        if(!loginMember.getRole().equals("MANAGER") && loginMember.getMemberId() != origin.getMemberId()){
            return "redirect:/posts";
        }

        if(bindingResult.hasErrors()){
            return "post/write";
        }

        postService.editPost(post);
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam int id, HttpSession session){
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if(loginMember == null){
            return "redirect:/login";
        }

        PostDto post = postService.getPost(id);
        if(!loginMember.getRole().equals("MANAGER") && loginMember.getMemberId() != post.getMemberId()){
            return "redirect:/login";
        }

        postService.removePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/restaurant/search")
    @ResponseBody
    public PostDto search(@RequestParam String keyword){
        return postService.findByName(keyword);
    }
}