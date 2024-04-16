package com.project.eat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {


//    프론트엔드 페이지 테스트를 위한 컨트롤러입니다.
    @GetMapping({"/", "/main"})
    public String main() {
        log.info("/main...");


        return "main";
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