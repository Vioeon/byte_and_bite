package net.likelion.bebc25.bytebite.news.repository;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTemplateNewsRepository implements NewsRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateNewsRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<NewsDto> postRowMapper = (ResultSet rs, int rowNum) -> {
//        return new PostDto(
//                rs.getInt("id"),
//                rs.getString("title"),
//                rs.getString("content"),
//                rs.getString("author"),
//                rs.getBoolean("secret"),
//                rs.getObject("created_at", LocalDateTime.class));
        return NewsDto.builder()
                    //.id(rs.getInt("id"))
                    .title(rs.getString("title"))
                    //.author(rs.getString("author"))
                    .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

    private final RowMapper<NewsDto> postDetailMapper = (ResultSet rs, int rowNum) -> {
        return NewsDto.builder()
                //.id(rs.getInt("id"))
                .title(rs.getString("title"))
                //.author(rs.getString("author"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
                //.content(rs.getString("content")).build();
    };

    @Override
    public List<NewsDto> findAll() {
        return jdbcTemplate.query("SELECT title, created_at FROM news", postRowMapper);
    }

}
