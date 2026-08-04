package net.likelion.bebc25.bytebite.mypage.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MypageRepositoryImpl implements MypageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MypageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberDto> memberRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .memberId(rs.getInt("member_id"))
                .nickname(rs.getString("nickname"))
                .email(rs.getString("email"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

    private final RowMapper<PostDto> postRowMapper = (ResultSet rs, int rowNum) -> {
        return PostDto.builder()
                .image(rs.getString("image"))
                .title(rs.getString("title"))
                .restaurantName(rs.getString("rname"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

    // id 에 해당하는 회원정보 찾기
    @Override
    public MemberDto findProfileById(int memberId) {
        return jdbcTemplate.queryForObject("SELECT member_id, nickname, email, created_at FROM member WHERE member_id = ?", memberRowMapper, memberId);
    }

    // id 에 해당하는 일반 사용자가 작성한 리뷰 목록 찾기
    @Override
    public List<PostDto> findPostListById(int memberId) {
        String sql = "SELECT p.image, p.title, p.created_at, r.rname " +
                     "FROM post p " +
                     "INNER JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                     "WHERE p.member_id = ? AND p.type = 'POST' ";

        return jdbcTemplate.query(sql, postRowMapper, memberId);
    }

    // id 에 해당하는 맛집 운영자가 작성한 소식 목록 찾기
    @Override
    public List<PostDto> findNewsListById(int memberId) {
        String sql = "SELECT p.image, p.title, p.created_at, r.rname " +
                "FROM post p " +
                "INNER JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "WHERE p.member_id = ? AND p.type = 'NEWS' ";

        return jdbcTemplate.query(sql, postRowMapper, memberId);
    }

}
