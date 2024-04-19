package com.project.eat.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//HandlerInterceptor를 상속받는 클래스를 구현 : 컴포넌트로 등록
//WebMvcConfigurer를 상속받는 클래스에서 DI해서 사용
@Slf4j
@Component
public class MemberRestInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		log.info("===============================================");
		log.info("==================== preHandle ====================");
		log.info("request url : {}", url);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("==================== postHandle ======================");
		log.info("===============================================");
	}
}
