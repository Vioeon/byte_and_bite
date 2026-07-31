package net.likelion.bebc25.bytebite.post.repository;

import net.likelion.bebc25.bytebite.post.dto.PostDto;
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
        return PostDto.builder()
                .id(rs.getInt("post_id"))
                .memberId(rs.getInt("member_id"))
                .nickname(rs.getString("nickname"))
                .title(rs.getString("title"))
                .image(rs.getString("image"))
                .category(rs.getString("category"))
                .restaurantName(rs.getString("rname"))
                .views(rs.getInt("view_count"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    private final RowMapper<PostDto> postDetailMapper = (ResultSet rs, int rowNum) -> {
        return PostDto.builder()
                .id(rs.getInt("post_id"))
                .memberId(rs.getInt("member_id"))
                .nickname(rs.getString("nickname"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .image(rs.getString("image"))
                .category(rs.getString("category"))
                .restaurantName(rs.getString("rname"))
                .views(rs.getInt("view_count"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    };

    @Override
    public List<PostDto> findAllPost() {
        String sql = "SELECT p.*, m.nickname, r.category, r.rname FROM post p "
                + "JOIN member m ON p.member_id = m.member_id "
                + "JOIN restaurant r ON p.restaurant_id = r.restaurant_id "
                + "WHERE p.type = 'POST'";

        return jdbcTemplate.query(sql, postRowMapper);
    }

    @Override
    public PostDto findById(int id) {
        String sql = "SELECT p.*, m.nickname, r.category, r.rname FROM post p JOIN member m ON p.member_id = m.member_id " +
                "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "WHERE p.post_id = ?";
        return jdbcTemplate.queryForObject(sql, postDetailMapper, id);
    }

    @Override
    public void save(PostDto post) {
        jdbcTemplate.update("INSERT INTO post (member_id, restaurant_id, title, content, image) VALUES (?, ?, ?, ?, ?)"
                , post.getMemberId()
                , post.getRestaurantId()
                , post.getTitle()
                , post.getContent()
                , post.getImage());
    }

    @Override
    public void update(PostDto post) {
        jdbcTemplate.update("UPDATE post SET title = ?, content = ? WHERE post_id = ?"
                , post.getTitle()
                , post.getContent()
                , post.getId());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM post WHERE post_id = ?", id);
    }
}
