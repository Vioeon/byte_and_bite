package net.likelion.bebc25.bytebite.news.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.bytebite.post.dto.NewPageDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.news.service.NewsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import net.likelion.bebc25.bytebite.file.FileStore;

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
    // file 로직 관련 구조체
    private final FileStore fileStore;

    public NewsController(NewsService newsService, @Qualifier("localFileStore") FileStore fileStore){
        this.newsService = newsService;
        this.fileStore = fileStore;
    }

    // 소식 목록 조회하는 컨트롤러
    @GetMapping // /news/list prefix
    public String getNewsBoardList(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "9") int size,
                                   @RequestParam(value = "sort", defaultValue = "latest") // 최신순, 조회순 정렬
                                   String sort, Model model){
        // 게시글 목록 조회(데이터)
        NewPageDto<PostDto> news = newsService.getNewsList(page, size, sort);

        model.addAttribute("news", news.getContent());
        model.addAttribute("pageResponse", news);
        model.addAttribute("sort", sort);
        model.addAttribute("menu", "news");
        return "news/newsList"; // template/admin/list(.html)
    }

    // 소식 상세 조회하는 컨트롤러
    @GetMapping("/{id}")
    public String getNewsDetail(@PathVariable int id, Model model){
        PostDto news = newsService.getNews(id);
        System.out.println("news = " + news);
        model.addAttribute("news", news);
        model.addAttribute("menu", "news");
        return "news/detail"; // 템플릿 파일 경로
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/write")
    public String getWriteNewsForm(@ModelAttribute("newsForm") PostDto post){
        return "news/write"; // 템플릿 파일 경로
    }

    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/write")
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post, // Validation 검증 대상 객체
                            BindingResult bindingResult){ // Validation 검증 결과 저장 객체(대상 객체 뒤에 기술해야 함)
        if(bindingResult.hasErrors()){ // 검증에 실패했을 경우
            return "news/write"; // 작성중이던 페이지로 다시 보낸다.
        }
        newsService.writeNews(post);
        return "redirect:/news"; // 브라우저에 /news로 재요청하라고 응답
    }
}