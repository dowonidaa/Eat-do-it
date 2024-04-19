package com.project.eat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@GetMapping({"/"})
	public String hello(Model model) {
		log.info("/hello...");

		return "thymeleaf/index";
	}

	//로그인페이지 input 기재한 id / pw 조회로 post방식이 원칙이나 임의로 쿠키생성을 위한 테스트용
	@GetMapping("/loginSuccess1")
	public String loginSuccess1(Model model, HttpServletResponse response) {
		log.info("/loginSuccess1...");

		String userId = "tester1";

		// 쿠키 생성 및 클라이언트에 전달
		Cookie cookie = new Cookie("userId", userId);
		cookie.setMaxAge(3600); // 쿠키의 유효 시간 설정 (초 단위)

		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}


	@GetMapping("/loginSuccess2")
	public String loginSucces2(Model model, HttpServletResponse response) {
		log.info("/loginSuccess2...");

		String userId = "tester2";

		// 쿠키 생성 및 클라이언트에 전달
		Cookie cookie = new Cookie("userId", userId);
		cookie.setMaxAge(3600); // 쿠키의 유효 시간 설정 (초 단위)
		response.addCookie(cookie);

		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}



}
