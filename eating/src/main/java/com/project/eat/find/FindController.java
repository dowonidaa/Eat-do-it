package com.project.eat.find;

import com.project.eat.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class FindController {
    @Autowired
    MemberService memberService;

    // 아이디 찾기 페이지
    @GetMapping({"/member/findID"})
    public String findID() {
        log.info("/member/findID...");


        return "member/findID";
    }

    @GetMapping("/member/findID_confirm")
    public String findID_confirm(@RequestParam(name = "email") String email, Model model) {
        log.info("/member/findID_confirm...");

        // 여기서 email 값을 이용하여 필요한 처리를 수행하고, 모델에 데이터를 추가할 수 있습니다.
        // 예를 들어, 이메일 값을 이용하여 해당 사용자의 아이디를 조회하고 모델에 추가할 수 있습니다.

        // 모델에 이메일 값을 추가하여 HTML에 전달합니다.
        model.addAttribute("email", email);

        return "member/findID_confirm";
    }

}
