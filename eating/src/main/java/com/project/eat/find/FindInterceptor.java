package com.project.eat.find;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class FindInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 요청 URL에서 이메일 파라미터를 가져옴
        String emailParam = request.getParameter("email");

        // 이메일 파라미터가 비어있는지 확인
        if (emailParam == null || emailParam.isEmpty()) {
            // 이메일 파라미터가 없는 경우, 403 Forbidden 응답 반환
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return false;
        }

        // 이메일 파라미터가 있으면 다음 Interceptor 또는 Controller로 요청을 전달
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("==================== postHandle ======================");
        log.info("===============================================");
    }
}
