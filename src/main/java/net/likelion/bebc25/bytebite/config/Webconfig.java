package net.likelion.bebc25.bytebite.config;

import net.likelion.bebc25.bytebite.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {
    // Spring MVC에서 로그인 체크할 때 사용해.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LoginCheckInterceptor가 true인경우만 컨트롤러 실행, 아니면 로그인 화면으로 이동
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/member/*", "/post/write", "/news/write", "/mypage/*") // 로그인 체크할 경로
                .excludePathPatterns("/member/mypage", "/member/posts", "/member/signup", "/member/signup/restaurant", "/member/login", "/post/list", "/news/list", "/member/register", "/css/**", "/js/**", "/*.ico", "/error"); // 제외할 경로
    }
}