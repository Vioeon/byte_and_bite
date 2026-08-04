package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    private final RestaurantRepository restaurantRepository;
    private final FileService fileService;
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, FileService fileService) {
        this.restaurantRepository = restaurantRepository;
        this.fileService = fileService;
    }

    @Override
    public RestaurantDto findByMemberId(int id) {
        return restaurantRepository.findById(id);
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public void updateRestaurant(int memberId, RestaurantDto restaurantDto) {
        RestaurantDto existingRestaurant = restaurantRepository.findById(memberId);
        String oldImagePath = existingRestaurant.getImageUrl();

        MultipartFile imageFile = restaurantDto.getImage();

        restaurantDto.setMemberId(memberId);

        // 새 이미지가 업로드된 경우
        // 이미지 파일이 존재하고 비어있지 않은 경우에만 이미지 저장 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                restaurantDto.setImageUrl(oldImagePath);

                String originalName = imageFile.getOriginalFilename();
                String savedName = UUID.randomUUID() + originalName;

                Path directory = Paths.get(uploadDir, "restaurant");
                Files.createDirectories(directory);
                imageFile.transferTo(directory.resolve(savedName));

                restaurantDto.setImageUrl(savedName);

                // db 이미지 변경
                restaurantRepository.update(memberId, restaurantDto);

                // 기존 이미지 삭제
                if (oldImagePath != null) {
                    fileService.delete(oldImagePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장에 실패했습니다.", e);
            }
        }else{
            // 이미지 변경 없으면 기존 이미지 유지
            restaurantDto.setImageUrl(oldImagePath);
            restaurantRepository.update(memberId, restaurantDto);
        }
    }
}
