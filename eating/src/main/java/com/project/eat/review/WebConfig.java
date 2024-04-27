package com.project.eat.review;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 배포시 아래 2개 모두 주석처리 
    private String connectPath = "/uploadimgPath/**";
    private String resourcePath = "file:///C:/upload/";

    // 배포시 하단 주석풀기
//    @Value("${upload.dir}")
//    private String uploadDir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);

//      배포시 하단 주석풀기 위에꺼 주석
//        log.info("{}",uploadDir);
//        registry.addResourceHandler(connectPath)
//                .addResourceLocations("file:///"+uploadDir);
        
        
        
    }
}
