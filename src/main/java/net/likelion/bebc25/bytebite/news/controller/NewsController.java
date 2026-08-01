package net.likelion.bebc25.bytebite.news.controller;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 맛집 소식 (GET /news)
// 맛집 소식 상세 조회 (GET /news/{newsId})
// 맛집 소식 작성 (GET /news/write)
// 맛집 소식 수정 (GET /news/edit)

@Controller // @Component의 구체화 어노테이션, html 페이지 user에게 return
@Slf4j
@RequestMapping("/news") // class level annotation, url prefix
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    // 소식 목록 조회하는 컨트롤러
    @GetMapping("/") // /news/list prefix
    public String getNewsBoardList(@RequestParam(value = "sort", defaultValue = "latest")
                                       String sort, Model model){
        // 게시글 목록 조회(데이터)
        List<NewsDto> news;
        if(sort.equals("latest")) { // 최신순 정렬
            news = newsService.getNews();
        }else{ // 조회수순 정렬
            news = newsService.getNewsByViews();
        }

        model.addAttribute("news", news); // Model transfer data to html(view)
        return "admin/newsList"; // template/admin/list(.html)
    }

    // 소식 상세 조회하는 컨트롤러
//    @GetMapping("/detail.html")
//    public String getNewsDetail(@RequestParam("id") int id, Model model){
//        NewsDto news = newsService.getNews(id);
//        model.addAttribute("news", news);
//        return "board/detail"; // 템플릿 파일 경로
//    }
}