package net.likelion.bebc25.bytebite.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadPath =
            "src/main/resources/static/uploads/";

    public String save(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalFilename = file.getOriginalFilename();

            String savedFilename = UUID.randomUUID() + "_" + originalFilename;
            Path uploadDir = Paths.get(uploadPath);

            // 폴더가 없으면 생성
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path path = uploadDir.resolve(savedFilename);
            Files.copy(
                    file.getInputStream(),
                    path
            );

            return "/uploads/" + savedFilename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }
}