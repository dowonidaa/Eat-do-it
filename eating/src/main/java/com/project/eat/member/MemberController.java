package com.project.eat.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private MemberService service;

    @Autowired
    private HttpSession session;

    @GetMapping("/member/insert")
    public String insert(Model model) {
        log.info("/member/insert...");

        return "member/insert";
    }

    //새로 추가한 거
    @GetMapping({"/member/login"})
    public String login(Model model) {
        log.info("/member/login...");


        return "member/login";
    }

    @GetMapping("/member/logout")
    public String logout() {
        log.info("/member/logout...");

        session.removeAttribute("member_id");

        return "redirect:/";
    }


    @PostMapping("/member/loginOK")
    public String loginOK(MemberVO_JPA vo, HttpServletRequest request, RedirectAttributes redirectAttributes) throws NoSuchAlgorithmException {
        log.info("/member/loginOK...");
        log.info("{}", vo);

        String salt = service.getSalt(vo);
        log.info("Salt : {}", salt);

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(), salt);
        log.info("암호화 결과 : {}", hex_password);
        vo.setPw(hex_password);

        MemberVO_JPA vo2 = service.loginOK(vo);
        log.info("{}", vo2);

        if (vo2 != null) {
            session.setAttribute("member_id", vo2.getId());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디와 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/login";
        }
    }

    @PostMapping("/member/insertOK")
    public String insertOK(MemberVO_JPA vo) {
        log.info("/member/insertOK...");
        log.info("vo:{}",vo);

        String salt = User_pwSHA512.Salt();
        log.info("Salt : {}",salt);
        //k8C2IX+McOvgwDRYrnjeLw==
        vo.setSalt(salt);//디비에 저장-복호화 할때 사용

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(),salt);//암호화
        log.info("암호화 결과 : {}", hex_password);
        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
        vo.setPw(hex_password);//디비에 저장

        MemberVO_JPA result = service.insertOK(vo);
        log.info("result:{}", result);

        if (result != null) {
            return "redirect:selectAll";
        } else {
            return "redirect:insert";
        }

    }

    @GetMapping("/member/selectAll")
    public String selectAll(@RequestParam(name="cpage", defaultValue = "1") int cpage,
                            @RequestParam(name="pageBlock", defaultValue = "5") int pageBlock,Model model) {
        log.info("/member/selectAll...");
        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

//		List<MemberVO_JPA> vos = service.selectAll();
        List<MemberVO_JPA> vos = service.selectAllPageBlock(cpage, pageBlock);

        //MemberDAO_JPA에서 비밀번호가 너무길어서 10글자만 잘라서 반환함.
        model.addAttribute("vos", vos);

        // member테이블에 들어있는 모든회원수는 몇명?
        long total_rows = service.getTotalRows();
        log.info("total_rows:" + total_rows);

        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }
        // 페이지 링크 몇개?
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);
//		model.addAttribute("totalPageCount", 10);//테스트용

        return "member/selectAll";
    }

    @GetMapping("/member/searchList")
    public String searchList(@RequestParam(name="cpage", defaultValue = "1") int cpage,
                             @RequestParam(name="pageBlock", defaultValue = "5") int pageBlock, String searchKey, String searchWord, Model model) {
        log.info("/member/searchList...");
        log.info("searchKey:{}", searchKey);
        log.info("searchWord:{}", searchWord);
        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

//		List<MemberVO_JPA> vos = service.searchList(searchKey,searchWord);
        List<MemberVO_JPA> vos = service.searchListPageBlock(searchKey, searchWord, cpage, pageBlock);

        model.addAttribute("vos", vos);


        // 키워드검색 모든회원수는 몇명?
        long total_rows = service.getSearchTotalRows(searchKey, searchWord);
        log.info("total_rows:" + total_rows);

        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }
        // 페이지 링크 몇개?
        model.addAttribute("totalPageCount", totalPageCount);
//		model.addAttribute("totalPageCount", 10);//테스트용


        return "member/selectAll";
    }

    @GetMapping("/member/selectOne")
    public String selectOne(MemberVO_JPA vo, Model model) {
        log.info("/member/selectOne...");
        log.info("vo:{}",vo);

        MemberVO_JPA vo2 = service.selectOne(vo);
        vo2.setPw(vo2.getPw().substring(0,10));
        model.addAttribute("vo2", vo2);

        return "member/selectOne";
    }

    @PostMapping("/member/updateOK")
    public String updateOK(MemberVO_JPA vo) {
        log.info("/member/updateOK...");
        log.info("vo:{}",vo);

        String salt = User_pwSHA512.Salt();
        log.info("Salt : {}",salt);
        //k8C2IX+McOvgwDRYrnjeLw==
        vo.setSalt(salt);//디비에 저장-복호화 할때 사용

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(),salt);//암호화
        log.info("암호화 결과 : {}", hex_password);
        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
        vo.setPw(hex_password);//디비에 저장

        //수정일자 반영안하면 null값이 들어가는 것을 방지하기위해...
        if(vo.getRegdate()==null) {
            vo.setRegdate(new Date());
        }

        MemberVO_JPA result = service.updateOK(vo);
        log.info("result:{}", result);

        return "redirect:selectAll";

    }

    @GetMapping("/member/delete")
    public String delete(Model model) {
        log.info("/member/delete...");

        return "member/delete";
    }

    // m_deleteOK 삭제시 반드시 @Transactional선언.
    @Transactional
    @PostMapping("/member/deleteOK")
    public String deleteOK(MemberVO_JPA vo) {
        log.info("/member/deleteOK...");
        log.info("vo:{}",vo);

        int result = service.deleteOK(vo);
        log.info("result:{}", result);

        return "redirect:selectAll";
    }


    @GetMapping({"/member/changePW"})
    public String changePW() {
        log.info("/member/changePW...");


        return "member/changePW";
    }

    @GetMapping({"/member/findID"})
    public String findID() {
        log.info("/member/findID...");


        return "member/findID";
    }

    @GetMapping({"/member/findID_confirm"})
    public String findID_confirm() {
        log.info("/member/findID_confirm...");


        return "member/findID_confirm";
    }

    @GetMapping({"/member/findPW"})
    public String findPW() {
        log.info("/member/findPW...");

        return "member/findPW";
    }

    @GetMapping({"/member/join"})
    public String join() {
        log.info("/member/join...");


        return "member/join";
    }

    @GetMapping({"/member/join_confirm"})
    public String join_confirm() {
        log.info("/member/join_confirm...");


        return "member/join_confirm";
    }

    @GetMapping({"/member/mypage"})
    public String mypage() {
        log.info("/member/mypage...");


        return "member/mypage";
    }

}
