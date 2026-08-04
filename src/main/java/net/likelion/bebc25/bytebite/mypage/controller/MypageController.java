package net.likelion.bebc25.bytebite.mypage.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.LoginDto;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.member.service.RestaurantService;
import net.likelion.bebc25.bytebite.mypage.service.MypageService;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final RestaurantService RestaurantService;

    public MypageController(MypageService mypageService, RestaurantService RestaurantService) {
        this.mypageService = mypageService;
        this.RestaurantService = RestaurantService;
    }

    @GetMapping
    public String getMemberProfile(HttpSession session, Model model) { // 리턴값은 String 타입, 회원정보 가져와라, 로그인했는지 확인, 현재는 비어 있는 Model 객체 임의로 생성

        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember"); // session 에 담아두었던 "loginMember" 의 회원정보 받아와라 // 다형성 관련

        if ("MANAGER".equals(loginMember.getRole())) { // 로그인한 계정이 맛집운영자 계정이라면

            // memberId로 식당 정보 조회하여 Dto에 저장
            RestaurantDto restaurant = RestaurantService.findByMemberId(loginMember.getMemberId()); // RestaurantDto 를 restaurant 변수라고 하겠다, 로그인한 아이디에 맞는 식당 정보 받아와라

            // 모델에 식당 정보 담기
            model.addAttribute("restaurant", restaurant); // model 에 식당정보를 추가해라
        }

        MemberDto memberInfo = mypageService.getProfileById(loginMember.getMemberId()); // MemberDto 를 memberInfo 변수라고 하겠다, 로그인한 아이디에 맞는 회원 정보를 받아와라
        List<PostDto> mypagePostList = mypageService.getPostListById(loginMember.getMemberId()); // List<PostDto> 를 mypagePostList 변수라고 하겠다, 로그인한 아이디에 맞는 리뷰목록을 받아와라

        model.addAttribute("memberInfo", memberInfo); // Model 에 회원정보를 넣어라
        model.addAttribute("mypagePostList", mypagePostList); // Model 에 작성한 리뷰목록을 넣어라
        model.addAttribute("menu", "mypage"); // Model 에 메뉴를 보여주고 마이페이지 메뉴가 활성화되도록 해라
        return "mypage/mypage"; // 마이페이지 화면을 보여줘라
    }

    // 식당 정보 수정 화면
    @GetMapping("/restaurant/edit")
    public String getRestaurantEditForm(HttpSession session, Model model) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        RestaurantDto restaurantDto = RestaurantService.findByMemberId(loginMember.getMemberId());
        model.addAttribute("restaurantForm", restaurantDto);
        return "mypage/restaurantEdit";
    }
    // 식당 정보 수정
    @PostMapping("/restaurant/edit")
    public String restaurantEdit(@Valid @ModelAttribute("restaurantForm") RestaurantDto restaurantDto,
                                 BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "mypage/restaurantEdit";
        }
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        RestaurantService.updateRestaurant(loginMember.getMemberId(), restaurantDto);

        return "redirect:/mypage";
    }
}
