package com.project.eat.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class UploadService {

    @Value("${upload.dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        // 업로드된 파일의 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 파일을 업로드할 디렉토리 경로 설정
        String filePath = uploadDir + File.separator + fileName;
        // 파일을 디렉토리에 저장
        File dest = new File(filePath);
        file.transferTo(dest);
        // 파일의 URL 생성
        String fileUrl = "/upload/" + fileName; // 이 부분은 실제 웹 애플리케이션의 URL로 변경해야 합니다.
//        String fileUrl = uploadDir+"/" + fileName; // 이 부분은 실제 웹 애플리케이션의 URL로 변경해야 합니다.
        log.info("UploadService 피알 url 생성 확인 fileUrl:{}",fileUrl);


        // 파일의 URL 반환
//        return fileUrl;

        //파일명만 db에 저장...
        return fileName;
    }
}
