package net.likelion.bebc25.bytebite.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 컨트롤러 앞단에서 실행된다.
    // 로그인 여부만 체크해서 리다이렉트 시킨다.
    // 로그인 체크 필수인 곳에 사용
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("loginMember") == null){
            log.info("로그인 안된 사용자의 요청: " + requestUri);
            // 미인증 사용자(비로그인)인 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/member/login");
            // HandlerInterceptor가 false 리턴할 경우 컨트롤러 핸들러를 실행하지 않는다.
            return false;
        }
        // HandlerInterceptor가 false 리턴할 경우 다음 HandlerInterceptor나 컨트롤러 핸들러를 실행한다.
        return true;
    }
}

