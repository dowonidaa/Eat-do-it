package com.project.eat.member;

import com.project.eat.address.AddService;
import com.project.eat.address.AddressDAO_JPA;
import com.project.eat.address.AddressVO_JPA;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Member;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private MemberService service;

    @Autowired
    private AddService service_add;

    @Autowired
    private MemberDAO_JPA jpa;

    @Autowired
    private AddressDAO_JPA jpa_add;

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

//    @PostMapping("/member/loginOK")
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
    //도원님 코드
    @PostMapping("/member/loginOK")
    public String loginOK(MemberVO_JPA vo, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "/")String redirectURL) throws NoSuchAlgorithmException {
        log.info("/member/loginOK...");
        log.info("{}", vo);
        log.info("redirectURL = {}",redirectURL );

        String salt = service.getSalt(vo);
        log.info("Salt : {}", salt);

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(), salt);
        log.info("암호화 결과 : {}", hex_password);
        vo.setPw(hex_password);

        MemberVO_JPA vo2 = service.loginOK(vo);
        log.info("{}", vo2);

        if (vo2 != null) {
            session.setAttribute("member_id", vo2.getId());
//            session.setAttribute("member", vo2.getNum());
            return "redirect:" + redirectURL;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디와 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/login";
        }
    }

    // 아이디 찾기 페이지
//    @GetMapping({"/member/findID"})
//    public String findID() {
//        log.info("/member/findID...");
//
//        public Map<String, String> findId(@RequestBody UserFindRequestDto requestDto) {
//            Map<String, String> id = userService.findId(requestDto);
//            return id;
//        }
//
//        return "member/findID";
//    }


    @GetMapping("/member/checkId")
    @ResponseBody
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        log.info("id check");

        boolean isAvailable = service.doesMemberExist(id);
        return ResponseEntity.ok(isAvailable);
    }


    @PostMapping("/member/insertOK")
    public String insertOK(MemberVO_JPA vo, AddressVO_JPA address) {

        log.info("/member/insertOK...");
        log.info("vo:{}",vo);
        log.info("address:{}",address);

        String salt = User_pwSHA512.Salt();
        log.info("Salt : {}",salt);
        //k8C2IX+McOvgwDRYrnjeLw==
        vo.setSalt(salt);//디비에 저장-복호화 할때 사용

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(),salt);//암호화
        log.info("암호화 결과 : {}", hex_password);
        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
        vo.setPw(hex_password);//디비에 저장

        MemberVO_JPA result = service.insertOK(vo);
        address.setMId(result);
        service_add.saveAddress(address);
        log.info("result:{}", result);

        if (result != null) {
            return "redirect:/";
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
        log.info("vo2:{}",vo2);

        return "member/selectOne";
    }




    //member 테이블 정보, address 테이블 정보
    @PostMapping("/member/updateOK")
    public String updateOK(MemberVO_JPA memberVO, AddressVO_JPA addressVO, HttpServletRequest request) {
        log.info("/member/updateOK...");
        log.info("addressVo ={}", addressVO);

        String newPassword = request.getParameter("pw");
        String newAddress = request.getParameter("address");
        String newEmail = request.getParameter("email");
        String dbEmail = request.getParameter("dbEmail");
        String newNickname = request.getParameter("nickname");
        String newTel = request.getParameter("tel");

        boolean isUpdate = false;

        // 비밀번호 변경
        // 새로운 비밀번호가 입력되었을 때만 업데이트
        if ((newPassword != null && !newPassword.isEmpty()) && (isUpdate == false)) {

            log.info("Original password: {}", memberVO.getPw()); // 원본 비밀번호 로깅
            log.info("memberVO:{}",memberVO);

            String salt = User_pwSHA512.Salt();
            log.info("Salt : {}",salt);
            //k8C2IX+McOvgwDRYrnjeLw==
            String hex_password = User_pwSHA512.getSHA512(newPassword, salt); //암호화
            log.info("암호화 결과 : {}", hex_password);
            //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
            //얘는 입력된 걸 다 받아서 비밀번호만 바꿈. 그래서 나머지도 다 바뀌는 거.
            memberVO.setPw(hex_password);//디비에 저장
            memberVO.setSalt(salt); //디비에 저장-복호화 할때 사용

            //수정일자 반영안하면 null값이 들어가는 것을 방지하기위해
            if(memberVO.getRegdate()==null) {
                memberVO.setRegdate(new Date());
            }
            
            // Member 정보를 각각 업데이트
            MemberVO_JPA updatedMember = service.updateOK(memberVO);
            log.info("Updated member: {}", updatedMember);

            isUpdate = true;
        }

        // 닉네임 변경
        if (newNickname != null && !newNickname.isEmpty() && (isUpdate == false)){
            //얘는 입력된 게 아니라, 원래 데이터를 가져옴.
            //비밀번호는 왜 공백 입력이 안되느냐 : 저중에 공백인 애가 비밀번호 뿐이니까, 쟤는 자동으로 나머지 값이 다 입력되는 것
            //그러면, 비밀번호 로직을 따서 memberVO에 pw만 따로 입력을 해주면 끝나는 문제 ! ! !
            //그래서 단독 업데이트가 비밀번호 로직을 쓰면 안 됐던 것..ㅜㅜㅜ


            //기존 닉네임과 비교하기 위해
            String existingNickname = jpa.findById(memberVO.getId()).getNickname();
            log.info("원래 닉네임 : {}", existingNickname);

            if (!existingNickname.equals(newNickname)){
                //비밀번호는 기본으로 입력되어 있지 않으니까, DB에서 id로 검색해서 따로 받아옴.
                String existingPW = jpa.findById(memberVO.getId()).getPw();

                memberVO.setNickname(newNickname);
                memberVO.setPw(existingPW);

                // Member 정보를 각각 업데이트
                MemberVO_JPA updatedMember = service.updateOK(memberVO);

                log.info("Updated nickname: {}", updatedMember);

                isUpdate = true;

            } else {
                log.info("기존 닉네임과 같습니다");
            }




        }

        // 전화번호 변경
        if (newTel != null && !newTel.isEmpty() && (isUpdate == false)){

            //기존 전화번호와 비교하기 위해
            String existingTel = jpa.findById(memberVO.getId()).getTel();
            log.info("원래 전화번호 : {}", existingTel);

            //existingTel 있는 tel이랑 tel이 같으면 -> update 안 함.
            if(!existingTel.equals(newTel)){
                //비밀번호는 기본으로 입력되어 있지 않으니까, DB에서 id로 검색해서 따로 받아옴.
                String existingPW = jpa.findById(memberVO.getId()).getPw();

                memberVO.setTel(newTel);
                memberVO.setPw(existingPW);

                // Member 정보를 각각 업데이트
                MemberVO_JPA updatedMember = service.updateOK(memberVO);

                log.info("Updated tel: {}", updatedMember);

                isUpdate = true;
            } else {
                log.info("기존 전화번호와 같습니다");
            }


        }

        // 이메일 변경
        // 조건문 추가 : newEmail 안 비어있고, update 안 된 상태인 거고, existing 관련 조건문까지(얘는 분기 안에) 추가.
        if (newEmail != null && !newEmail.isEmpty() && (isUpdate == false)){

            //기존 이메일과 비교하기 위해
            String existingEmail = jpa.findById(memberVO.getId()).getEmail();
            log.info("원래 이메일 : {}", existingEmail);
            
            if(!existingEmail.equals(newEmail)){
                //비밀번호는 기본으로 입력되어 있지 않으니까, DB에서 id로 검색해서 따로 받아옴.
                String existingPW = jpa.findById(memberVO.getId()).getPw();

                memberVO.setEmail(newEmail);
                memberVO.setPw(existingPW);

                // Member 정보를 각각 업데이트
                MemberVO_JPA updatedMember = service.updateOK(memberVO);

                log.info("Updated email: {}", updatedMember);

                isUpdate = true;
            } else {
                log.info("기존 이메일과 같습니다");
            }

        }


        // 주소 변경
        if (newAddress != null && !newAddress.isEmpty()){
            AddressVO_JPA existingAddress = jpa_add.findBymId(memberVO); // 기존의 주소 정보를 조회
            if (existingAddress == null){
                existingAddress = new AddressVO_JPA();
                existingAddress.setMId(memberVO);
            }

            log.info("existingAddress: {}", existingAddress);
            log.info("addressVO.getAddress(): {}", addressVO.getAddress());

            existingAddress.setAddress(addressVO.getAddress());

            AddressVO_JPA updatedAddress = service_add.updateOK(existingAddress);

            log.info("Updated address: {}", updatedAddress);
        }


        return "redirect:/";

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
    public String mypage(Model model, HttpSession session) {
        log.info("/member/mypage...");
        // 세션에서 로그인한 사용자의 아이디 가져오기 : member 테이블에 사용할 거
        String memberId = (String) session.getAttribute("member_id");
        log.info("Logged in member ID: {}", memberId);

        // 로그인한 사용자의 아이디를 이용하여 해당 회원 정보 조회
        MemberVO_JPA memberVO = new MemberVO_JPA();
        memberVO.setId(memberId);

        // 회원 정보 조회
        MemberVO_JPA memberInfo = service.selectOneById(memberVO);
        log.info("Member info: {}", memberInfo);


        // 멤버 아이디를 사용하여 해당 멤버 정보를 데이터베이스에서 조회 : address 테이블에 사용할 거
        MemberVO_JPA memberId_add = jpa.findById(memberVO.getId());

        // 주소 정보 조회
        AddressVO_JPA addressVO = new AddressVO_JPA();
        //memberId를 MemberVO_JPA로 바꿔야함.
        addressVO.setMId(memberId_add);

        // 주소 정보 조회
        AddressVO_JPA addressInfo = service_add.selectOneById(addressVO);
//        log.info("Address info: {}", addressInfo);

        if (memberInfo != null) {
            memberInfo.setPw(memberInfo.getPw().substring(0, 10));
            model.addAttribute("memberInfo", memberInfo);
        } else {
            // 회원 정보가 없는 경우에 대한 처리
            log.error("Member not found for ID: {}", memberId);
            return "redirect:/"; // 예시로 에러 페이지로 리다이렉트
        }

        if (addressInfo != null) {
            model.addAttribute("addressInfo", addressInfo);
        } else {
            log.warn("Address not found for member ID: {}", memberId);
        }

        return "member/mypage";
    }

}
