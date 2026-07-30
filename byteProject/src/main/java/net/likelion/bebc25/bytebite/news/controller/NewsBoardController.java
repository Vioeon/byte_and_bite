package net.likelion.bebc25.bytebite.news.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // @Component의 구체화 어노테이션, html 페이지 user에게 return
@Slf4j
@RequestMapping("/") // class leve annotation, url prefix
public class NewsBoardController {

    private final NewsService newsService;

    public NewsBoardController(NewsService newsService){
        this.newsService = newsService;
    }

    // 소식 목록 조회하는 컨트롤러
    @GetMapping("news") // /news
    public String getNewsBoardList(Model model){
        // 게시글 목록 조회(데이터)
        List<NewsDto> news = newsService.getNews();
        model.addAttribute("news", news);
        return "board/list.html";
    }

    // 소식 상세 조회하는 컨트롤러
//    @GetMapping("/detail.html")
//    public String getNewsDetail(@RequestParam("id") int id, Model model){
//        NewsDto news = newsService.getNews(id);
//        model.addAttribute("news", news);
//        return "board/detail"; // 템플릿 파일 경로
//    }
}