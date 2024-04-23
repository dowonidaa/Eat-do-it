package com.project.eat.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//HandlerInterceptor를 상속받는 클래스를 구현 : 컴포넌트로 등록
//WebMvcConfigurer를 상속받는 클래스에서 DI해서 사용
@Slf4j
@Component
public class MemberInterceptor implements HandlerInterceptor {
	
	@Autowired
	HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		log.info("===============================================");
		log.info("==================== preHandle ====================");
		log.info("request url : {}", url);
		
		String member_id = (String)session.getAttribute("member_id");
		log.info("session - member_id : {}", member_id);
		
		if(member_id == null) {
			
			if(url.equals("/member/selectAll") || url.equals("/member/selectOne")
					|| url.equals("/member/searchList")) {
				response.sendRedirect("login");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("==================== postHandle ======================");
		log.info("===============================================");
	}
}
