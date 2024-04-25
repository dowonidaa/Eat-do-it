package com.project.eat.review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class ImageCall {
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        log.info("ImageCall !!! 확인 - imageName: {}", imageName);

        try {
            // 이미지 파일의 경로를 설정합니다.
//            String imagePath = "C:/upload/" + imageName;
            String imagePath = "C:/upload/" + imageName;
            Path path = Paths.get(imagePath);

            // 파일 확장자를 가져옵니다.
            String extension = StringUtils.getFilenameExtension(imagePath);

            // 이미지 파일을 바이트 배열로 읽어옵니다.
            byte[] imageData = Files.readAllBytes(path);

            // Content-Type을 결정합니다.
            MediaType mediaType;
            switch (extension.toLowerCase()) {
                case "png":
                case "PNG":
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                case "jpg":
                case "jpeg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                case "gif":
                    mediaType = MediaType.IMAGE_GIF;
                    break;
                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    break;
            }

            // 응답 헤더를 설정하여 Content-Type을 이미지 MIME 타입으로 지정합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            // 이미지 데이터와 헤더를 포함하는 ResponseEntity를 반환합니다.
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 이미지를 가져오는 도중에 예외가 발생한 경우 처리합니다.
            log.error("이미지를 가져오는 도중에 예외가 발생했습니다.", e);
            // 적절한 응답을 반환하거나 예외를 다시 던지거나 처리 방법에 따라 적절하게 처리합니다.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

