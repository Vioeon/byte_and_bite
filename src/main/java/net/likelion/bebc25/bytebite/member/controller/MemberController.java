package net.likelion.bebc25.bytebite.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.exception.DuplicateEmailException;
import net.likelion.bebc25.bytebite.exception.DuplicateNicknameException;
import net.likelion.bebc25.bytebite.member.dto.*;
import net.likelion.bebc25.bytebite.member.service.MemberService;
import net.likelion.bebc25.bytebite.member.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final RestaurantService restaurantService;

    public MemberController(MemberService memberService, RestaurantService restaurantService) {
        this.memberService = memberService;
        this.restaurantService = restaurantService;
    }

    // 마이페이지 화면 테스트용
    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");
        if(loginMember != null && loginMember.getRole().equals("MANAGER")) {

            // memberId로 식당 정보 조회하여 Dto에 저장
            RestaurantDto restaurant = restaurantService.findByMemberId(loginMember.getMemberId());
            log.info(restaurant.getImageUrl());
            model.addAttribute("restaurant", restaurant);
        }
        log.info(loginMember.getNickname());
        model.addAttribute("menu", "mypage");
        return "mypage/mypage";
    }


    // 회원가입 화면
    @GetMapping("/signup")
    public String getSignupForm(@ModelAttribute("signupForm") SignupDto signupDto) {
        return "member/signup";
    }
    // 회원가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupForm") SignupDto signupDto,
                           BindingResult bindingResult, HttpSession session, Model model) {
        // 비밀번호 일치 확인
        if (!signupDto.getPassword().equals(signupDto.getPasswordConfirm())) {
            bindingResult.rejectValue(
                    "passwordConfirm",
                    "passwordMismatch",
                    "비밀번호가 일치하지 않습니다."
            );
        }
        if (bindingResult.hasErrors()) {
            return "member/signup";
        }

        // 일반 사용자, 맛집 운영자 - 이메일 중복 체크
        try{
            memberService.validateDuplicateEmail(signupDto);
        }catch (DuplicateEmailException e){
            bindingResult.rejectValue("email", "duplicate", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup";
        }
        // 닉네임 중복 체크
        try {
            memberService.validateDuplicateNickname(signupDto);
        } catch (DuplicateNicknameException e) {
            bindingResult.rejectValue("nickname", "duplicate", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup";
        }
        // 맛집 운영자인 경우 식당 정보 입력으로
        if(signupDto.getRole().equals("MANAGER")){
            session.setAttribute("signupForm", signupDto);
            return "redirect:/member/signup/restaurant";
        }
        // 일반 사용자 가입 처리
        memberService.signup(signupDto);

        return "redirect:/member/login";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String getLoginForm(@ModelAttribute("loginForm") LoginDto loginDto) {
        return "member/login";
    }
    // 로그인 인증 요청 처리
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginDto loginDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {

        if(bindingResult.hasErrors()){ // 검증에 실패했을 경우
            log.info("검증 실패");
            log.info("errors={}", bindingResult.getAllErrors());
            return "member/login"; // 작성중이던 페이지로 다시 보낸다.
        }

        // 로그인 시도
        MemberDto memberInfo = memberService.login(loginDto.getEmail(), loginDto.getPassword());
        if(memberInfo == null){
            // 로그인 실패 메시지를 담아 다시 로그인 페이지로 리다이렉트
            // addFlashAttribute: 임시로 세션에 속성을 담아서 리다이렉트 된 페이지에서 꺼내어 사용 후 속성값은 세션에서 제거함
            redirectAttributes.addFlashAttribute("loginErrorMessage", "아이디 또는 비밀번호를 확인하세요.");
            redirectAttributes.addFlashAttribute("loginForm", loginDto); // 입력 폼 데이터 유지
            return "redirect:/member/login";
        }
        // 로그인 성공 시
        // 세션 생성하여 사용자 정보 저장
        SessionMemberDto sessionMember = new SessionMemberDto(memberInfo);
        session.setAttribute("loginMember", sessionMember);
        // 로그인한 세션 10분 설정
        session.setMaxInactiveInterval(600);

        return "redirect:/posts";
    }

    // 맛집 정보 등록 화면
    @GetMapping("/signup/restaurant")
    public String getResSignupForm(@ModelAttribute("restaurantForm") RestaurantDto restaurantDto) {
        return "member/resSignup";
    }
    // 맛집 정보 등록
    @PostMapping("/signup/restaurant")
    public String restaurantSignup(@Valid @ModelAttribute("restaurantForm") RestaurantDto restaurantDto,
            BindingResult bindingResult, HttpSession session) {
        // 식당 정보 검증
        if (bindingResult.hasErrors()) {
            return "member/resSignup";
        }
        // 회원가입 때 임시 저장한 회원정보 가져오기
        SignupDto signupDto = (SignupDto) session.getAttribute("signupForm");
        if(signupDto == null){
            // 세션 만료 시 처음으로
            return "redirect:/member/signup";
        }
//        MultipartFile images = restaurantDto.getImage(); // asdfg

        // 회원 + 식당 저장
        memberService.signupWithRestaurant(signupDto, restaurantDto);

        // 사용한 세션 제거
        session.removeAttribute("signupForm");
        return "redirect:/member/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); // 세션 파기
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login"; // 비로그인 상태면 로그인 페이지로
        }
        memberService.withdraw(loginMember.getMemberId());
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/";
    }
}
