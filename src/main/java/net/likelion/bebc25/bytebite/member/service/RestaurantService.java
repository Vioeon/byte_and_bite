package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;

public interface RestaurantService {

    RestaurantDto findByMemberId(int id);

    void updateRestaurant(int memberId, RestaurantDto restaurantDto);
}
