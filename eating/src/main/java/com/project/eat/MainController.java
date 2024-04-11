package com.project.eat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {


//    프론트엔드 페이지 테스트를 위한 컨트롤러입니다.
    @GetMapping({"/", "/main"})
    public String main() {
        log.info("/main...");


        return "thymeleaf/main";
    }

    @GetMapping({"/changePW"})
    public String changePW() {
        log.info("/changePW...");


        return "thymeleaf/changePW";
    }

    @GetMapping({"/login"})
    public String login() {
        log.info("/login...");


        return "thymeleaf/login";
    }

    @GetMapping({"/findID"})
    public String findID() {
        log.info("/findID...");


        return "thymeleaf/findID";
    }

    @GetMapping({"/findID_confirm"})
    public String findID_confirm() {
        log.info("/findID_confirm...");


        return "thymeleaf/findID_confirm";
    }

    @GetMapping({"/findPW"})
    public String findPW() {
        log.info("/findPW...");

        return "thymeleaf/findPW";
    }

    @GetMapping({"/join"})
    public String join() {
        log.info("/join...");


        return "thymeleaf/join";
    }

    @GetMapping({"/join_confirm"})
    public String join_confirm() {
        log.info("/join_confirm...");


        return "thymeleaf/join_confirm";
    }

    @GetMapping({"/mypage"})
    public String mypage() {
        log.info("/mypage...");


        return "thymeleaf/mypage";
    }
    @GetMapping({"/pay"})
    public String pay() {
        log.info("/pay...");


        return "thymeleaf/pay";
    }
    @GetMapping({"/pay_confirm"})
    public String pay_confirm() {
        log.info("/pay_confirm...");


        return "thymeleaf/pay_confirm";
    }

    @GetMapping({"/pay_out"})
    public String pay_out() {
        log.info("/pay_out...");


        return "thymeleaf/pay_out";
    }
    @GetMapping({"/shop_detail"})
    public String shop_detail() {
        log.info("/shop_detail...");


        return "thymeleaf/shop_detail";
    }

    @GetMapping({"/shop_list"})
    public String shop_list() {
        log.info("/shop_list...");


        return "thymeleaf/shop_list";
    }

    @GetMapping({"/shop_review"})
    public String shop_review() {
        log.info("/shop_review...");


        return "thymeleaf/shop_review";
    }









}