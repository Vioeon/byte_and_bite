package net.likelion.bebc25.bytebite.news.repository;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
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

    private final RowMapper<NewsDto> newsRowMapper = (ResultSet rs, int rowNum) -> {
        return NewsDto.builder()
                    .newsId(rs.getInt("news_id"))
                    .title(rs.getString("title"))
                    //.author(rs.getString("author"))
                    .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

//    private final RowMapper<NewsDto> newsDetailMapper = (ResultSet rs, int rowNum) -> {
//        return NewsDto.builder()
//                //.id(rs.getInt("id"))
//                .title(rs.getString("title"))
//                //.author(rs.getString("author"))
//                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
//                //.content(rs.getString("content")).build();
//    };

    @Override
    public List<NewsDto> findAll() { // findAll query
        return jdbcTemplate.query("SELECT news_id, title, created_at FROM news ORDER BY created_at DESC", newsRowMapper);
    }

}
