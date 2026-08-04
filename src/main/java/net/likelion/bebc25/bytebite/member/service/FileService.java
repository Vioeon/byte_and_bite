package net.likelion.bebc25.bytebite.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadPath = "src/main/resources/static/uploads/restaurant/";

    public String save(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Path uploadDir = Paths.get(uploadPath);

            // 폴더가 없으면 생성
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            // uuid로 파일명 중복 방지
            String originalFilename = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID() + "_" + originalFilename;

            // 최종 저장 경로
            Path path = uploadDir.resolve(savedFilename);
            Files.copy(file.getInputStream(), path);

            // db 저장
            return "/uploads/restaurant/" + savedFilename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }

    public void delete(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            Path path = Paths.get("src/main/resources/static" + imagePath);
            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제 실패: " + imagePath, e);
        }
    }
}