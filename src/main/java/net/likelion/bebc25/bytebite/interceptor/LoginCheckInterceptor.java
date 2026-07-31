package net.likelion.bebc25.bytebite.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 컨트롤러 앞단에서 실행된다.
    // 로그인 여부만 체크해서 리다이렉트
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("loginMember") == null){
            log.info("로그인 안된 사용자의 요청: " + requestUri);
            response.sendRedirect("/member/login");
            return false;
        }
        return true;
    }
}

