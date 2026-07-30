package net.likelion.bebc25.bytebite.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.exception.DuplicateEmailException;
import net.likelion.bebc25.bytebite.member.dto.*;
import net.likelion.bebc25.bytebite.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // -----------------------------------------------------

    // 맛집리뷰 화면 테스트용
    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("menu", "posts");
        return "post/postList";
    }
    // 마이페이지 화면 테스트용
    @GetMapping("/mypage")
    public String mypage(Model model) {
        model.addAttribute("menu", "mypage");
        return "mypage/mypage";
    }
    // -----------------------------------------------------

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
        // @Valid 검증 및 에러 메시지
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "errorMessage",
                    bindingResult.getAllErrors().get(0).getDefaultMessage()
            );
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
    public String getLoginForm(@ModelAttribute("loginForm") MemberDto memberDto) {
        return "member/login";
    }
    // 로그인 인증 요청 처리
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") MemberDto memberDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {

        if(bindingResult.hasErrors()){ // 검증에 실패했을 경우
            return "member/login"; // 작성중이던 페이지로 다시 보낸다.
        }

        // 로그인 시도
        MemberDto memberInfo = memberService.login(memberDto.getEmail(), memberDto.getPassword());
        if(memberInfo == null){
            // 로그인 실패 메시지를 담아 다시 로그인 페이지로 리다이렉트
            // addFlashAttribute: 임시로 세션에 속성을 담아서 리다이렉트 된 페이지에서 꺼내어 사용 후 속성값은 세션에서 제거함
            redirectAttributes.addFlashAttribute("loginErrorMessage", "아이디 또는 비밀번호를 확인하세요.");
            redirectAttributes.addFlashAttribute("loginForm", memberDto); // 입력 폼 데이터 유지
            return "member/login";
        }
        // 로그인 성공 시
        // 세션 생성하여 사용자 정보 저장
        SessionMemberDto sessionMember = new SessionMemberDto(memberInfo);
        session.setAttribute("loginMember", sessionMember);

        return "redirect:/member/list";
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
//    /**
//     * 전체 회원 목록을 조회하고 회원 목록 정적 화면으로 유도합니다.
//     *
//     * @param model 화면에 전달할 데이터를 담는 Model 객체
//     * @return 회원 목록 화면으로의 redirect 경로
//     */
//    @GetMapping("/list.html")
//    public String getMemberList(Model model) {
//        // 실습 영역
//        List<MemberDto> members = memberService.getMembers();
//        model.addAttribute("members", members);
//        return "member/list";
//    }
//
//    /**
//     * 회원 정보 수정 화면으로 유도합니다.
//     *
//     * @param id    수정할 회원의 일련번호
//     * @param model 화면에 전달할 데이터를 담는 Model 객체
//     * @return 회원 정보 수정 화면으로의 redirect 경로
//     */
//    @GetMapping("/edit.html")
//    public String getEditForm(@RequestParam("id") int id, Model model) {
//        // 실습 영역
//        MemberDto memberDto = memberService.getMember(id);
//        model.addAttribute("memberForm", memberDto);
//        return "member/edit";
//    }
//
//    /**
//     * 회원 정보 수정 요청 데이터를 받아 반영 처리를 수행합니다.
//     *
//     * @param memberDto 수정 요청 데이터 DTO
//     * @return 회원 목록 화면으로의 redirect 경로
//     */
//    @PostMapping("/edit")
//    public String edit(@Valid @ModelAttribute("memberForm") MemberDto memberDto,
//                       BindingResult bindingResult) {
//        // 실습 영역
//        if (bindingResult.hasErrors()) {
//            return "member/edit";
//        }
//        memberService.modifyInfo(memberDto);
//        return "redirect:/member/list.html";
//    }
//
//    /**
//     * 회원 탈퇴 요청을 받아 삭제 처리를 수행합니다.
//     *
//     * @param id 탈퇴할 회원의 일련번호
//     * @return 회원 목록 화면으로의 redirect 경로
//     */
//    @PostMapping("/withdraw")
//    public String withdraw(@RequestParam int id) {
//        // 실습 영역
//        memberService.withdraw(id);
//        return "redirect:/member/list.html";
//    }
}
