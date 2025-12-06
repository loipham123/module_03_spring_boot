package com.sqc.acedemy.bai_4.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Được gọi ngay sau khi bean được khởi tạo.
     * Nếu thư mục upload chưa tồn tại thì tự động tạo để tránh lỗi khi ghi file.
     */
    @PostConstruct
    public void createUploadDirIfNeeded() {
        try {
            Path path = Paths.get(uploadDir);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception ex) {
            log.error("Could not create upload directory at {}", uploadDir, ex);
        }
    }

    /**
     * Map URL /images/** tới thư mục vật lý D:/uploads/images để có thể truy cập ảnh qua HTTP.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(uploadDir).toUri().toString();
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}