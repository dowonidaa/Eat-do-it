package com.project.eat.admin;


import com.project.eat.member.MemberVO_JPA;
import com.project.eat.member.User_pwSHA512;
import com.project.eat.shop.ShopService;
import com.project.eat.shop.ShopVO;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class AdminController {
    @Autowired
    AdminService service;

    @GetMapping("/admin/admin")
    public String admin(@RequestParam(name="cpage", defaultValue = "1") int cpage,
                        @RequestParam(name="pageBlock", defaultValue = "5") int pageBlock, Model model, HttpSession session) {
        log.info("/admin/admin...");
        String memberId = (String) session.getAttribute("member_id");
        log.info("Logged in member ID: {}", memberId);

        // memberId가 null이거나 admin이 아닌 경우 리다이렉트
        if (memberId == null || !memberId.equals("admin")) {
            return "redirect:/"; // 홈 페이지로 리다이렉트
        }



        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

//		List<MemberVO_JPA> vos = service.selectAll();
        List<AdminVO_JPA> vos = service.selectAllPageBlock(cpage, pageBlock);

        model.addAttribute("vos", vos);

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

        // admin인 경우 admin 페이지로 이동
        return "admin/admin";
    }

    @GetMapping("/admin/selectOne")
    public String selectOne(AdminVO_JPA vo, Model model) {
        log.info("/admin/selectOne...");
        log.info("vo:{}",vo);

        AdminVO_JPA vo2 = service.selectOne(vo);
        model.addAttribute("vo2", vo2);
        log.info("vo2:{}",vo2);

        return "admin/selectOne";
    }

    @GetMapping("/admin/delete")
    public String delete(Model model) {
        log.info("/admin/delete...");

        return "admin/admin_delete";
    }

    // m_deleteOK 삭제시 반드시 @Transactional선언.
    @Transactional
    @PostMapping("/admin/deleteOK")
    public String deleteOK(AdminVO_JPA vo) {
        log.info("/admin/deleteOK...");
        log.info("vo:{}",vo);

        int result = service.deleteOK(vo);
        log.info("result:{}", result);

        return "redirect:admin";
    }

    @PostMapping("/admin/insertOK")
    public String insertOK(AdminVO_JPA vo) {
        log.info("/admin/insertOK...");
        log.info("vo:{}",vo);

        AdminVO_JPA result = service.insertOK(vo);
        log.info("result:{}", result);

        if (result != null) {
            return "redirect:admin";
        } else {
            return "redirect:insert";
        }

    }

    @GetMapping("/admin_confirm")
    public String admin_confirm(HttpSession session, Model model) {
        log.info("/admin_confirm...");
        return "admin/admin_confirm";
    }

    @GetMapping({"/insertShop"})
    public String insertShop() {
        log.info("/admin/insertShop...");


//        // 모든 Admin 테이블의 행을 가져옵니다.
//        List<AdminVO_JPA> adminList = service.selectAll();
//
//        // 콘솔에 모든 행을 출력합니다.
//        for (AdminVO_JPA admin : adminList) {
//            log.info("Admin Info: {}", admin);
//        }

        return "admin/insertShop";
    }

    @PostMapping("/insertShopOK")
    public String insertShopOK(AdminVO_JPA vo) {
        log.info("/insertShopOK...");
        log.info("vo:{}",vo);


        AdminVO_JPA result = service.insertOK(vo);
        log.info("result:{}", result);

        if (result != null) {
            return "redirect:admin_confirm";
        } else {
            return "redirect:insertShopOK";
        }

    }

    //shop 테이블에 insert(음식점 등록이 승인된 경우)
    @Autowired
    private ShopService shopService;

    @GetMapping("/admin/approve")
    public String approveShopOK(@RequestParam("adminId") int adminId, Model model) {
        // 주어진 adminId로 AdminVO_JPA 객체 가져오기
        AdminVO_JPA adminVO = service.findByAdminId(adminId);

        if (adminVO != null) {
            // AdminVO_JPA를 ShopVO로 변환 후 shop 테이블에 삽입
            ShopVO shopVO = shopService.approveShop(adminVO);
            log.info("shopVO:{}", shopVO);
            if (shopVO != null) {
                model.addAttribute("message", "음식점 등록 승인이 완료되었습니다!");
                service.deleteOK(adminVO);
            } else {
                model.addAttribute("message", "음식점 등록 승인에 실패했습니다.");
            }
        } else {
            model.addAttribute("message", "AdminVO를 찾을 수 없습니다.");
        }

        return "admin/approvalResult"; // 결과를 보여주는 페이지로 리디렉션
    }
//    @PostMapping("/approveShopOK")
//    public String approveShopOK(AdminVO_JPA adminVO) {
//        // adminVO에서 shopVO로 변환하여 shop 테이블에 삽입
//        ShopVO result = shopService.approveShop(adminVO);
//
//        if (result != null) {
//            return "redirect:/admin"; // 성공적으로 삽입된 경우
//        } else {
//            return "redirect:/"; // 삽입 실패 시
//        }
//    }

}
