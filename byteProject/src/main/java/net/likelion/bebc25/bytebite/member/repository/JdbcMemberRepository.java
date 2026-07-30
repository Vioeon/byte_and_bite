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

/**
 * Spring의 JdbcTemplate을 사용하여 회원 데이터를 처리하는 저장소 구현체입니다.
 */
@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 생성자를 통해 의존하는 JdbcTemplate을 주입받습니다.
     *
     * @param jdbcTemplate 스프링 빈으로 등록된 JdbcTemplate 객체
     */
    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 데이터베이스 ResultSet 데이터를 MemberDto 객체로 변환해주는 맵퍼 정의입니다.
     */
    private final RowMapper<MemberDto> membersRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .id(rs.getInt("id"))
                .nickname(rs.getString("nickname")) // 아이디
                .email(rs.getString("email"))
                .role(rs.getInt("role"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    private final RowMapper<MemberDto> memberDetailRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .id(rs.getInt("id"))
                .nickname(rs.getString("nickname")) // 아이디
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .role(rs.getInt("role"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    private final RowMapper<MemberDto> loginRowMapper = (ResultSet rs, int rowNum) -> {
        return MemberDto.builder()
                .id(rs.getInt("id"))
                .nickname(rs.getString("username")) // 아이디
                .password(rs.getString("password"))
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
            ps.setInt(4, signupDto.getRole());

            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * {@inheritDoc} username 회원 조회
     */
    @Override
    public MemberDto findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, username, password, created_at FROM member2 WHERE username = ?", loginRowMapper, username);
        } catch (EmptyResultDataAccessException e) {
            // Spring JDB Exception
            // 값이 존재하지 않을 때
            return null;
        }
    }

    /**
     * {@inheritDoc} id 회원 조회
     */
    @Override
    public MemberDto findById(int id) {
        return jdbcTemplate.queryForObject("SELECT id, username, password, email, created_at FROM member2 WHERE id = ?", memberDetailRowMapper, id);
    }

    /**
     * {@inheritDoc} 회원 정보 수정
     */
    @Override
    public void update(MemberDto member) {
        // 실습 영역
        jdbcTemplate.update("UPDATE member2 SET nickname = ?, password = ?, email = ? WHERE id = ?"
                , member.getNickname()
                , member.getPassword()
                , member.getEmail()
                , member.getId());
    }

    /**
     * {@inheritDoc} id 회원 삭제
     */
    @Override
    public void deleteById(int id) {
        // 실습 영역
        jdbcTemplate.update("DELETE FROM member2 WHERE id = ?", id);
    }

    /**
     * {@inheritDoc} 회원 목록 조회
     */
    @Override
    public List<MemberDto> findAll() {
        return jdbcTemplate.query("SELECT id, username, email, created_at FROM member2", membersRowMapper);
    }
}
