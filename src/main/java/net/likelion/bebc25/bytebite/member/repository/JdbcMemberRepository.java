package net.likelion.bebc25.bytebite.member.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberDto> membersRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .memberId(rs.getInt("id"))
                .nickname(rs.getString("nickname")) // 아이디
                .email(rs.getString("email"))
                .role(rs.getString("role"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    private final RowMapper<MemberDto> memberDetailRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .memberId(rs.getInt("member_id"))
                .nickname(rs.getString("nickname")) // 아이디
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .role(rs.getString("role"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    // 신규 회원 저장 + pk값 반환
    @Override
    public int save(SignupDto signupDto) {
        // keyHolder 사용해서 INSERT 후 PK 값 가져옴
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (nickname, password, email, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, signupDto.getNickname());
            ps.setString(2, signupDto.getPassword());
            ps.setString(3, signupDto.getEmail());
            ps.setString(4, signupDto.getRole());

            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    // email 회원 조회
    @Override
    public MemberDto findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT member_id, nickname, email, password, role, created_at FROM member WHERE email = ?", memberDetailRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // id 회원 조회
    @Override
    public MemberDto findById(int id) {
        return jdbcTemplate.queryForObject("SELECT id, username, password, email, created_at FROM member WHERE id = ?", memberDetailRowMapper, id);
    }

    // 이메일 중복 확인
    @Override
    public boolean existsByEmail(String email) {
        // Integer 타입으로 받음
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void update(MemberDto member) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<MemberDto> findAll() {
        return List.of();
    }

}
