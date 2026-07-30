package net.likelion.bebc25.bytebite.member.repository;

import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRestaurantRepository implements RestaurantRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcRestaurantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 신규 식당 정보 저장
    @Override
    public void save(RestaurantDto restaurantDto) {
        jdbcTemplate.update("INSERT INTO restaurant(member_id, category, rname, address, phone, image) VALUES (?,?,?,?,?,?)"
                , restaurantDto.getMemberId()
                , restaurantDto.getCategory()
                , restaurantDto.getRname()
                , restaurantDto.getAddress()
                , restaurantDto.getPhone()
                , restaurantDto.getImageUrl());
    }
}
