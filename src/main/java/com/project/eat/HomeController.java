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

		//쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
		Cookie idCookie = new Cookie("userid", "tester1");
		response.addCookie(idCookie);

		String userId = idCookie.getValue();
		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}


	@GetMapping("/loginSuccess2")
	public String loginSucces2(Model model, HttpServletResponse response) {
		log.info("/loginSuccess2...");

		//쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
		Cookie idCookie = new Cookie("userid", "tester2");
		response.addCookie(idCookie);

		String userId = idCookie.getValue();
		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}



}
