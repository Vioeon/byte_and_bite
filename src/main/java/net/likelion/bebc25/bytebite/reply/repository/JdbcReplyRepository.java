package net.likelion.bebc25.bytebite.reply.repository;

import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcReplyRepository implements ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReplyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReplyDto> replyRowMapper = (ResultSet rs, int rowNum) -> {
        return ReplyDto.builder()
                .replyId(rs.getInt("reply_id"))
                .postId(rs.getInt("post_id"))
                .memberId(rs.getInt("member_id"))
                .nickname(rs.getString("nickname"))
                .content(rs.getString("content"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .build();
    };

    @Override
    public List<ReplyDto> findByPostId(int postId) {

        String sql = """
                SELECT r.*, m.nickname
                FROM reply r
                JOIN member m
                    ON r.member_id = m.member_id
                WHERE r.post_id = ?
                ORDER BY r.created_at ASC
                """;

        return jdbcTemplate.query(sql, replyRowMapper, postId);
    }
}