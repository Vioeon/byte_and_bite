package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

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

    @Override
    public void updateRestaurant(int memberId, RestaurantDto restaurantDto) {
        RestaurantDto existingRestaurant = restaurantRepository.findById(memberId);

        String imagePath = existingRestaurant.getImageUrl();

        // 새 이미지가 업로드된 경우
        // 이미지 파일이 존재하고 비어있지 않은 경우에만 이미지 저장 처리
        if (restaurantDto.getImage() != null && !restaurantDto.getImage().isEmpty()) {
            // 기존 이미지 삭제
            if (imagePath != null) {
                fileService.delete(imagePath);
            }
            // 새 이미지 저장
            imagePath = fileService.save(restaurantDto.getImage());
        }

        restaurantDto.setMemberId(memberId);
        restaurantDto.setImageUrl(imagePath);

        restaurantRepository.update(memberId, restaurantDto);
    }
}
