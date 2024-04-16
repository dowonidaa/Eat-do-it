package com.project.eat.find;

import com.project.eat.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class FindController {
    @Autowired
    FindService findService;

    // 아이디 찾기 페이지
    @GetMapping({"/member/findID"})
    public String findID() {
        log.info("/member/findID...");


        return "member/findID";
    }

    @GetMapping("/member/findID_confirm")
    public String findID_confirm(@RequestParam(name = "email") String email, Model model) {
        log.info("/member/findID_confirm...");

        // 이메일을 통해 아이디를 찾음
        List<String> foundId = findService.findId(email);
        if (foundId != null) {
            // 아이디가 존재할 경우, 아이디 확인 페이지로 이동
            model.addAttribute("foundId", foundId);
            return "member/findID_confirm";
        } else {
            // 아이디가 존재하지 않을 경우, 다시 아이디 찾기 페이지로 이동
            return "redirect:/member/findID";
        }
    }

    @GetMapping({"/member/findPW"})
    public String findPW() {
        log.info("/member/findPW...");

        return "member/findPW";
    }

    @GetMapping({"/member/changePW"})
    public String changePW(@RequestParam(name = "email") String email, Model model) {
        log.info("/member/changePW...");

        // 이메일을 통해 아이디를 찾음
        List<String> foundId = findService.findId(email);
        if (foundId != null) {
            // 아이디가 존재할 경우, 비밀번호 변경 페이지로 이동
            model.addAttribute("foundId", foundId);
            return "member/changePW";
        } else {
            // 아이디가 존재하지 않을 경우, 다시 아이디 찾기 페이지로 이동
            return "redirect:/member/findPW";
        }


    }


}
