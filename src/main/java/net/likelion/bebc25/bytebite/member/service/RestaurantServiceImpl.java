package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantDto findByMemberId(int id) {
        return restaurantRepository.findById(id);
    }
}
