package net.likelion.bebc25.bytebite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {

        return "redirect:/news/list"; // 게시글 목록으로 이동
    }
}