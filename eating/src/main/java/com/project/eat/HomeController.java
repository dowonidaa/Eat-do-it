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

	//  메인으로 가는 경로 수정
	@GetMapping({"/", "/main"})
	public String main() {
		log.info("/main...");


		return "main";
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

	@GetMapping({"/pay/pay"})
	public String pay() {
		log.info("/pay/pay...");
		return "pay/pay";
	}

	@GetMapping({"/pay/pay_confirm"})
	public String pay_confirm() {
		log.info("/pay/pay_confirm...");
		return "pay/pay_confirm";
	}

	@GetMapping({"/pay/pay_out"})
	public String pay_out() {
		log.info("/pay/pay_out...");
		return "pay/pay_out";
	}

	@GetMapping({"/shop/shop_detail"})
	public String shop_detail() {
		log.info("/shop/shop_detail...");
		return "shop/shop_detail";
	}

	@GetMapping({"/shop/shop_list"})
	public String shop_list() {
		log.info("/shop/shop_list...");
		return "shop/shop_list";
	}

	@GetMapping({"/shop/shop_review"})
	public String shop_review() {
		log.info("/shop/shop_review...");
		return "shop/shop_review";
	}

	@GetMapping({"/shop/findLocation"})
	public String findLocation() {
		log.info("/shop/findLocation...");
		return "shop/findLocation";
	}





}
