package com.project.eat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestParam;
=======
>>>>>>> 3bd15674ea199b733d29ddf6704fed247ce757e5

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
<<<<<<< HEAD
	public String loginSuccess1(Model model, HttpServletResponse response) {// @RequestParam("userId") String userId
		log.info("/loginSuccess1...");

		// 로그인 성공한 경우
		// 사용자 ID를 어딘가에서 가져와야 합니다. 현재는 테스트용으로 "tester1"을 사용합니다.
		String userId = "tester1";

		// 쿠키 생성 및 클라이언트에 전달
		Cookie cookie = new Cookie("userId", userId);
		cookie.setMaxAge(3600); // 쿠키의 유효 시간 설정 (초 단위)
		response.addCookie(cookie);
=======
	public String loginSuccess1(Model model, HttpServletResponse response) {
		log.info("/loginSuccess1...");

		//쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
		Cookie idCookie = new Cookie("userid", "tester1");
		response.addCookie(idCookie);

		String userId = idCookie.getValue();
>>>>>>> 3bd15674ea199b733d29ddf6704fed247ce757e5
		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}


	@GetMapping("/loginSuccess2")
	public String loginSucces2(Model model, HttpServletResponse response) {
		log.info("/loginSuccess2...");

<<<<<<< HEAD
		// 사용자 ID를 어딘가에서 가져와야 합니다. 현재는 테스트용으로 "tester2"을 사용합니다.
		String userId = "tester2";

		// 쿠키 생성 및 클라이언트에 전달
		Cookie cookie = new Cookie("userId", userId);
		cookie.setMaxAge(3600); // 쿠키의 유효 시간 설정 (초 단위)
		response.addCookie(cookie);
=======
		//쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
		Cookie idCookie = new Cookie("userid", "tester2");
		response.addCookie(idCookie);

		String userId = idCookie.getValue();
>>>>>>> 3bd15674ea199b733d29ddf6704fed247ce757e5
		model.addAttribute("userId", userId);

		return "thymeleaf/shop/shopMainPage";
	}



}
