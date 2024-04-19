package com.project.eat.find;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class FindService {

    @Autowired
    FindMapper findMapper;
    @Autowired
    JavaMailSender mailSender;



    // 인증번호를 저장할 Map 객체 생성
    private Map<String, String> verificationCodes = new HashMap<>();

    //이메일이 전송되지 않았을 때의 Exception
    public class EmailSendException extends RuntimeException {
        public EmailSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void sendVerificationCode(String email) {
        try {
            // 6자리의 랜덤한 인증번호 생성
            String verificationCode = generateVerificationCode();

            // 인증번호를 해당 이메일 주소에 전송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("먹고해 인증번호입니다.");
            message.setText("인증번호: " + verificationCode);

            mailSender.send(message);

            // 이메일 주소와 인증번호를 매핑하여 저장
            verificationCodes.put(email, verificationCode);
        } catch (Exception ex) {
            // 이메일 전송이 실패한 경우
            log.error("이메일 전송 실패: " + ex.getMessage());
            throw new RuntimeException("이메일 전송 실패", ex);
        }
    }

    public boolean verify(String email, String inputCode) {
        // 이메일 주소에 해당하는 저장된 인증번호 확인
        String storedCode = verificationCodes.get(email);

        log.info("저장된 코드: {}", storedCode);
        log.info("사용자가 입력한: {}", inputCode);

        // 저장된 인증번호와 사용자가 입력한 인증번호 비교
        if (storedCode != null && storedCode.equals(inputCode)) {
            // 인증번호가 일치하면 해당 이메일 주소로 등록된 아이디를 찾아 전송
            List<String> usernames = findMapper.findId(email);
            if (!usernames.isEmpty()) {
//                sendUsernames(email, usernames);
                return true;
            }
        }
        return false;
    }

    public boolean verify2(String email, String inputCode) {
        // 이메일 주소에 해당하는 저장된 인증번호 확인
        String storedCode = verificationCodes.get(email);

        log.info("저장된 코드: {}", storedCode);
        log.info("사용자가 입력한: {}", inputCode);

        // 저장된 인증번호와 사용자가 입력한 인증번호 비교
        if (storedCode != null && storedCode.equals(inputCode)) {
            log.info("일치함");
            return true;
        }
        return false;
    }

    // 6자리의 랜덤한 인증번호 생성 메서드
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public List<String> findId(String email) {
        return findMapper.findId(email);
    }

//    public void sendUsernames(String email, List<String> usernames) {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(email);
//        simpleMailMessage.setSubject("아이디 찾기");
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("가입하신 아이디는");
//        sb.append(System.lineSeparator());
//
//        for(int i=0;i<usernames.size()-1;i++) {
//            sb.append(usernames.get(i));
//            sb.append(System.lineSeparator());
//        }
//        sb.append(usernames.get(usernames.size()-1)).append("입니다");
//
//        simpleMailMessage.setText(sb.toString());
//
//
//        new Thread(new Runnable() {
//            public void run() {
//                mailSender.send(simpleMailMessage);
//            }
//        }).start();
//    }


}
