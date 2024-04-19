package com.project.eat.find;

import com.project.eat.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class FindRestController {
    @Autowired
    FindService findService;
    @Autowired
    MemberService memberService;

    // 메일로 인증번호 전송
    @PostMapping("/api/find/sendVerificationCode")
    public ResponseEntity<Object> sendVerificationCode(@RequestParam("email") String email){
        log.info("email:{}", email);

        findService.sendVerificationCode(email);
        log.info("메일이 정상적으로 전송되었습니다."); //메일 주소가 이상해도 찍힘..
        return new ResponseEntity<>("인증번호가 해당 이메일 주소로 전송되었습니다.", HttpStatus.OK);

    }

    // 입력된 인증번호 확인
    @PostMapping("/api/find/verifyCode")
    public ResponseEntity<Object> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code){
        boolean verified = findService.verify(email, code);
        if (verified) {
            return new ResponseEntity<Object>(HttpStatus.OK);
            //return ResponseEntity.ok("인증이 완료되었습니다. 아이디가 해당 이메일 주소로 전송되었습니다.");
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            //return ResponseEntity.badRequest().body("인증번호가 일치하지 않거나 이메일로 등록된 아이디가 없습니다.");
        }
    }

    // 입력된 인증번호 확인 : 마이페이지
    @PostMapping("/api/find/verifyCode2")
    public ResponseEntity<Object> verifyCode2(@RequestParam("email") String email, @RequestParam("code") String code){
        boolean verified = findService.verify2(email, code);
        if (verified) {
            return new ResponseEntity<Object>(HttpStatus.OK);
            //return ResponseEntity.ok("인증이 완료되었습니다. 아이디가 해당 이메일 주소로 전송되었습니다.");
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            //return ResponseEntity.badRequest().body("인증번호가 일치하지 않거나 이메일로 등록된 아이디가 없습니다.");
        }
    }

//    //아이디에서만 쓰는 거. 비밀번호는 재설정 할 거라서.
//    @PostMapping("/api/find/sendUsernames")
//    public ResponseEntity<Object> sendUsernames(@RequestParam("email") String email){
//        // 이메일 주소를 사용하여 아이디를 찾습니다.
//        List<String> usernames = findService.findId(email);
//
//        // 반환된 아이디를 확인하고 있으면 OK 응답으로 반환하고, 없으면 BadRequest 응답을 반환합니다.
//        if(usernames.size() != 0) {
//            // 아이디가 존재하는 경우
//            return ResponseEntity.ok(usernames);
//        } else {
//            // 아이디가 존재하지 않는 경우
//            return ResponseEntity.badRequest().body("해당 이메일 주소로 등록된 아이디가 없습니다.");
//        }
//    }

}
