package com.project.eat.find;

import com.project.eat.address.AddressVO_JPA;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.member.User_pwSHA512;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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

    @PostMapping("/member/findID_confirm")
    public String findID_confirm(@RequestParam(name = "email") String email, Model model) {
        log.info("/member/findID_confirm...");
        log.info("email:{}", email);
        // 이메일을 통해 아이디를 찾음
        List<String> foundId = findService.findId(email); //타입바꾸기
        log.info("foundId:{}", foundId);
        if (foundId != null) {
            // 아이디가 존재할 경우, 아이디 확인 페이지로 이동
            model.addAttribute("foundId", foundId.get(0)); //타입바꾸기
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

    @PostMapping({"/member/changePW"})
    public String changePW(@RequestParam(name = "email") String email, MemberVO_JPA vo, Model model) {
        log.info("/member/changePW...");
        log.info("Email: {}", email);


        if (email != null && !email.isEmpty()) {
            // 이메일이 존재하는 경우
            // 이메일을 통해 회원 정보 조회
            MemberVO_JPA vo2 = memberService.selectOneByEmail(vo);
            if (vo2 != null) {
//                vo2.setPw(vo2.getPw().substring(0,10));
                vo2.setPw(vo2.getPw());
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

    //member 테이블 정보, address 테이블 정보
    @PostMapping("/member/changePWOK")
    public String updateOK(MemberVO_JPA memberVO, HttpServletRequest request) {
        log.info("/member/changePWOK...");


        // 비밀번호 변경
        String newPassword = request.getParameter("pw");
        if (newPassword != null && !newPassword.isEmpty()) {
            //Member 정보 수정
            log.info("Original password: {}", memberVO.getPw()); // 원본 비밀번호 로깅
            log.info("memberVO:{}",memberVO);

            // 새로운 비밀번호가 입력되었을 때만 업데이트
            String salt = User_pwSHA512.Salt();
            log.info("Salt : {}",salt);
            //k8C2IX+McOvgwDRYrnjeLw==
            String hex_password = User_pwSHA512.getSHA512(newPassword, salt); //암호화
            log.info("암호화 결과 : {}", hex_password);
            //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
            memberVO.setPw(hex_password);//디비에 저장
            memberVO.setSalt(salt); //디비에 저장-복호화 할때 사용
        } else {
            log.info("입력된 비밀번호 없음");
        }



        //수정일자 반영안하면 null값이 들어가는 것을 방지하기위해
        if(memberVO.getRegdate()==null) {
            memberVO.setRegdate(new Date());
        }


        // Member 정보를 각각 업데이트
        MemberVO_JPA updatedMember = memberService.updateOK(memberVO);

        log.info("Updated member: {}", updatedMember);

        return "redirect:/";

    }


}
