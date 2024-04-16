package com.project.eat.find;

import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
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
    public String changePW(@RequestParam(name = "email") String email, MemberVO_JPA vo, Model model) {
        log.info("/member/changePW...");
        log.info("Email: {}", email);


        if (email != null && !email.isEmpty()) {
            // 이메일이 존재하는 경우
            // 이메일을 통해 회원 정보 조회
            MemberVO_JPA vo2 = memberService.selectOneByEmail(vo);
            if (vo2 != null) {
                vo2.setPw(vo2.getPw().substring(0,10));
                model.addAttribute("vo2", vo2);
                log.info("vo2: {}", vo2);
                return "member/changePW";
            } else {
                // 이메일에 해당하는 회원 정보가 없는 경우
                log.error("No member found for email: {}", email);
                return "redirect:/member/findPW"; // 비밀번호 찾기 페이지로 리다이렉션
            }
        } else {
            // 이메일이 없는 경우
            log.error("Email parameter is missing or empty");
            return "redirect:/member/findPW"; // 비밀번호 찾기 페이지로 리다이렉션
        }



    }


}
