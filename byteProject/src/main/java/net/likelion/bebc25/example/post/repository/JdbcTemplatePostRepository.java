package net.likelion.bebc25.example.post.repository;

import net.likelion.bebc25.example.post.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTemplatePostRepository implements PostRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePostRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PostDto> postRowMapper = (ResultSet rs, int rowNum) -> {
//        return new PostDto(
//                rs.getInt("id"),
//                rs.getString("title"),
//                rs.getString("content"),
//                rs.getString("author"),
//                rs.getBoolean("secret"),
//                rs.getObject("created_at", LocalDateTime.class));
        return PostDto.builder()
                    .id(rs.getInt("id"))
                    .title(rs.getString("title"))
                    .author(rs.getString("author"))
                    .createdAt(rs.getObject("created_at", LocalDateTime.class))
                    .secret(rs.getBoolean("secret")).build();
    };

    private final RowMapper<PostDto> postDetailMapper = (ResultSet rs, int rowNum) -> {
        return PostDto.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .content(rs.getString("content"))
                .secret(rs.getBoolean("secret")).build();
    };

    @Override
    public List<PostDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM post2", postRowMapper);
    }

    @Override
    public PostDto findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM post2 WHERE id = ?", postDetailMapper, id);
    }

    @Override
    public void save(PostDto post) {
        jdbcTemplate.update("INSERT INTO post2 (title, author, content) VALUES (?, ?, ?)"
                , post.getTitle()
                , post.getAuthor()
                , post.getContent());
    }

    @Override
    public void update(PostDto post) {
        jdbcTemplate.update("UPDATE post2 SET title = ?, author = ?, content = ? WHERE id = ?"
                , post.getTitle()
                , post.getAuthor()
                , post.getContent()
                , post.getId());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM post2 WHERE id = ?", id);
    }
}
