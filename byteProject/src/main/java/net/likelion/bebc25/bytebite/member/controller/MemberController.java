package net.likelion.bebc25.bytebite.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.LoginDto;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;
import net.likelion.bebc25.bytebite.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 회원 관련 요청(회원 가입, 로그인, 정보 수정, 탈퇴 등)을 처리하여 해당 화면 또는 동작으로 분기하는 컨트롤러 클래스입니다.
 */
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 생성자를 통해 MemberService 의존성을 주입받습니다.
     *
     * @param memberService 주입받을 MemberService 스프링 빈 객체
     */
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String getSignupForm(@ModelAttribute("SignupForm") SignupDto signupDto) {
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
            model.addAttribute(
                    "errorMessage",
                    bindingResult.getAllErrors().get(0).getDefaultMessage()
            );
            return "member/signup";
        }

        // 맛집 운영자
        if(signupDto.getRole() == 1){
            session.setAttribute("signupForm", signupDto);
            return "redirect:/member/signup/restaurant";
        }

        // 일반 사용자
        memberService.signup(signupDto);
        return "redirect:/member/login.html";
    }

    // 맛집 정보 등록 화면으로 이동
    @GetMapping("/signup/restaurant")
    public String getResSignupForm(@ModelAttribute("restaurantForm") RestaurantDto restaurantDto) {
        return "member/resSignup";
    }

    @PostMapping("/signup/restaurant")
    public String restaurantSignup(@Valid @ModelAttribute("restaurantForm") RestaurantDto restaurantDto,
            BindingResult bindingResult, HttpSession session) {

        // 식당 정보 검증
        if (bindingResult.hasErrors()) {
            return "member/resSignup";
        }

        // 회원가입 때 임시 저장한 회원정보 가져오기
        SignupDto signupDto = (SignupDto) session.getAttribute("signupForm");

        // 회원 + 식당 저장
        memberService.signupWithRestaurant(signupDto, restaurantDto);

        // 사용한 세션 제거
        session.removeAttribute("signupForm");

        return "redirect:/member/login";
    }


    @GetMapping("/resSignup")
    public String resSignup() {
        return "member/resSignup";
    }

    /**
     * 전체 회원 목록을 조회하고 회원 목록 정적 화면으로 유도합니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 회원 목록 화면으로의 redirect 경로
     */
    @GetMapping("/list.html")
    public String getMemberList(Model model) {
        // 실습 영역
        List<MemberDto> members = memberService.getMembers();
        model.addAttribute("members", members);
        return "member/list";
    }


    // 로그인 화면으로 이동
    @GetMapping("/login.html")
    public String getLoginForm(@ModelAttribute("loginForm") MemberDto memberDto) {
        return "member/login";
    }

    /**
     * 로그인 인증 요청을 처리합니다.
     *
     * @param username 로그인 요청 사용자 아이디(고유 식별 ID)
     * @param password 로그인 요청 사용자 비밀번호
     * @return 회원 목록 화면으로의 redirect 경로
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginDto loginDto,
                        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 1. @NotBlank 검사
        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        // 2. 로그인 검사
        MemberDto memberDto = memberService.login(loginDto.getUsername(), loginDto.getPassword());

        // 여긴 AI 도움 받음
        // 일치하는 아이디 없음
        if (memberDto == null) {
            // bindingResult.rejectValue 특정 필드 에러 처리
            // username 필드에 에러 메시지 추가
            bindingResult.rejectValue(
                    "username",
                    "loginFail",
                    "존재하지 않는 아이디입니다."
            );

            return "member/login";
        }
        // 비밀번호가 틀림
        else if (!memberDto.getPassword().equals(loginDto.getPassword())) {
            // password 필드에 에러 메시지 추가
            bindingResult.rejectValue(
                    "password",
                    "loginFail",
                    "비밀번호가 일치하지 않습니다."
            );

            return "member/login";
        }
        // 로그인 성공
        else {
            // RedirectAttributes 리다이렉트할 때 데이터를 잠깐 전달하기 위한 객체
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    memberDto.getNickname() + " 님 환영합니다!");

            return "redirect:/member/list.html";
        }
    }

    /**
     * 회원 정보 수정 화면으로 유도합니다.
     *
     * @param id    수정할 회원의 일련번호
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 회원 정보 수정 화면으로의 redirect 경로
     */
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model) {
        // 실습 영역
        MemberDto memberDto = memberService.getMember(id);
        model.addAttribute("memberForm", memberDto);
        return "member/edit";
    }

    /**
     * 회원 정보 수정 요청 데이터를 받아 반영 처리를 수행합니다.
     *
     * @param memberDto 수정 요청 데이터 DTO
     * @return 회원 목록 화면으로의 redirect 경로
     */
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("memberForm") MemberDto memberDto,
                       BindingResult bindingResult) {
        // 실습 영역
        if (bindingResult.hasErrors()) {
            return "member/edit";
        }
        memberService.modifyInfo(memberDto);
        return "redirect:/member/list.html";
    }

    /**
     * 회원 탈퇴 요청을 받아 삭제 처리를 수행합니다.
     *
     * @param id 탈퇴할 회원의 일련번호
     * @return 회원 목록 화면으로의 redirect 경로
     */
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam int id) {
        // 실습 영역
        memberService.withdraw(id);
        return "redirect:/member/list.html";
    }
}
