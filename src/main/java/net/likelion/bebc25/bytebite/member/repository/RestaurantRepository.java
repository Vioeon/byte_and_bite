package net.likelion.bebc25.bytebite.member.repository;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;

public interface RestaurantRepository {

    // 식당 정보 등록
    void save(RestaurantDto restaurantDto);

    // Id 식당 정보 조회
    RestaurantDto findById(int id);
}
