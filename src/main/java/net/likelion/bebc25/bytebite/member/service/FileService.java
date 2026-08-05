package net.likelion.bebc25.bytebite.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void delete(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            // /uploads/restaurant/abc.png 제거
            String filePath = imagePath.replace("/uploads/", "");

            Path path = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제 실패: " + imagePath, e);
        }
    }
}