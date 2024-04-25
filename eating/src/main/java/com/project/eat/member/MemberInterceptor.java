package com.project.eat.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.beans.Encoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
		String queryString = request.getQueryString();
		log.info("===============================================");
		log.info("==================== preHandle ====================");
		log.info("request url : {}", url);
		log.info("request queryString : {}", queryString);

		String member_id = (String)session.getAttribute("member_id");
		log.info("session - member_id : {}", member_id);
		String fullUrl = url + (queryString != null ? "?"+ queryString : "");
		if(member_id == null) {
			
			if(url.equals("/member/selectAll") || url.equals("/member/selectOne")
					|| url.equals("/member/searchList") || url.equals("/shopDetail") || url.equals("/orders")) {
				response.sendRedirect("/member/login?redirectURL=" + URLEncoder.encode(fullUrl, StandardCharsets.UTF_8));
				return false;
			}
		}
//
//		// 비밀번호 변경 접근 제한
//		if(url.equals("/member/changePW")) {
//			String emailParam = request.getParameter("email");
//			if (emailParam == null || emailParam.isEmpty()) {
//				log.info("접근제한");
//				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
//				return false;
//			}
//		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("==================== postHandle ======================");
		log.info("===============================================");
	}
}
