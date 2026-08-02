package net.likelion.bebc25.bytebite.mypage.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.member.service.RestaurantService;
import net.likelion.bebc25.bytebite.mypage.service.MypageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getMemberProfile(HttpSession session, Model model) {

        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/member/login";
        }

        if ("MANAGER".equals(loginMember.getRole())) {

            // memberId로 식당 정보 조회하여 Dto에 저장
            RestaurantDto restaurant = RestaurantService.findByMemberId(loginMember.getMemberId());

            // 모델에 식당 정보 담기
            if (restaurant != null) {
                model.addAttribute("restaurant", restaurant);
            }
        }

        MemberDto memberInfo = mypageService.getProfileById(loginMember.getMemberId());

        if (memberInfo != null) {
            log.info("조회된 닉네임: {}", memberInfo.getNickname());
        }
        model.addAttribute("memberInfo", memberInfo);

        return "mypage/mypage";
    }
}
