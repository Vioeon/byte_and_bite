package net.likelion.bebc25.bytebite.member.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;

@Repository
public class JdbcRestaurantRepository implements RestaurantRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcRestaurantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RestaurantDto> restaurantDetailRowMapper = (ResultSet rs, int rowNum) -> {
        return RestaurantDto.builder()
                .memberId(rs.getInt("restaurant_id"))
                .category(rs.getString("category")) // 아이디
                .rname(rs.getString("rname"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .imageUrl(rs.getString("image"))
                .build();
    };

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

    // 맛집 운영자 회원 정보 조회
    @Override
    public RestaurantDto findById(int id) {
        return jdbcTemplate.queryForObject("SELECT restaurant_id, category, rname, address, phone, image FROM restaurant WHERE member_id = ?", restaurantDetailRowMapper, id);
    }
}
