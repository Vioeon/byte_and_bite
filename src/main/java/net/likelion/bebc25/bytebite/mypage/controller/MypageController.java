package net.likelion.bebc25.bytebite.mypage.controller;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.service.MypageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    public MypageController(MypageService mypageService){
        this.mypageService = mypageService;
    }

    @GetMapping("/")
    public String getMemberProfile(Model model) {
        MemberDto hello = mypageService.getProfileById(2);

        model.addAttribute("memberinfo", hello);
        return "mypage/mypage";
    }
}
