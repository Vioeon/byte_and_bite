package net.likelion.bebc25.bytebite.mypage.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;

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

    // id 에 해당하는 회원정보 찾기
    @Override
    public MemberDto findProfileById(int memberId) {
        return jdbcTemplate.queryForObject("SELECT member_id, nickname, email, created_at FROM member WHERE member_id = ?", memberRowMapper, memberId);
    }
}
