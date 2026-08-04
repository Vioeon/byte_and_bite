package net.likelion.bebc25.bytebite.post.repository;

import net.likelion.bebc25.bytebite.post.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplatePostRepository implements PostRepository{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PostDto> searchMapper =
            (rs,rowNum)->PostDto.builder()
                    .restaurantId(rs.getInt("restaurant_id"))
                    .restaurantName(rs.getString("rname"))
                    .build();

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
    public List<PostDto> findAllPost(int offset, int pageSize) {
        String sql =
                "SELECT p.*, m.nickname, r.category, r.rname " +
                        "FROM post p " +
                        "JOIN member m ON p.member_id = m.member_id " +
                        "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                        "WHERE p.type='POST' " +
                        "ORDER BY p.post_id DESC " +
                        "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, postRowMapper, pageSize, offset);
    }

    @Override
    public PostDto findById(int id) {
        String sql = "SELECT p.*, m.nickname, r.category, r.rname FROM post p JOIN member m ON p.member_id = m.member_id " +
                "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "WHERE p.post_id = ?";
        return jdbcTemplate.queryForObject(sql, postDetailMapper, id);
    }

    // 최신순 조회
    @Override
    public List<PostDto> findLatest(int offset, int limit){
        String sql = "SELECT p.*, m.nickname, r.category, r.rname " +
                "FROM post p " +
                "JOIN member m ON p.member_id = m.member_id " +
                "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "ORDER BY p.created_at DESC " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, postRowMapper, limit, offset);
    }

    // 조회수순 조회
    @Override
    public List<PostDto> findPopular(int offset, int limit){
        String sql = "SELECT p.*, m.nickname, r.category, r.rname " +
                "FROM post p " +
                "JOIN member m ON p.member_id = m.member_id " +
                "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "ORDER BY p.view_count DESC " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, postRowMapper, limit, offset);
    }

    @Override
    public int countPost() {
        String sql =
                "SELECT COUNT(*) FROM post WHERE type='POST'";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public void save(PostDto post) {
        jdbcTemplate.update("INSERT INTO post (member_id, restaurant_id, type, title, content, image) VALUES (?, ?, ?, ?, ?, ?)"
                , post.getMemberId()
                , post.getRestaurantId()
                , post.getType()
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

    @Override
    public PostDto findByName(String keyword){
        String sql= "SELECT restaurant_id,rname " +
                "FROM restaurant " +
                "WHERE rname LIKE ?";

        List<PostDto> list=
                jdbcTemplate.query(sql, searchMapper, "%"+keyword+"%");

        if(list.isEmpty())
            return null;

        return list.get(0);
    }

    @Override
    public void increaseView(int postId){
        String sql= "UPDATE post " +
                "SET view_count=view_count+1 " +
                "WHERE post_id=?";

        jdbcTemplate.update(sql,postId);
    }

    // news
    private final RowMapper<PostDto> newsRowMapper = (ResultSet rs, int rowNum) -> {
        return PostDto.builder()
                .id(rs.getInt("post_id"))
                .restaurantId(rs.getInt("restaurant_id"))
                .restaurantName(rs.getString("rname"))
                .category(rs.getString("category"))
                .title(rs.getString("title"))
                .views(rs.getInt("view_count"))
                .image(rs.getString("image"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

    private final RowMapper<PostDto> newsDetailMapper = (ResultSet rs, int rowNum) -> {
        return PostDto.builder()
                .id(rs.getInt("post_id"))
                .restaurantId(rs.getInt("restaurant_id"))
                .nickname(rs.getString("nickname"))
                .restaurantName(rs.getString("rname"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .category(rs.getString("category"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .views(rs.getInt("view_count"))
                .image(rs.getString("image"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
    };

    // 작성일시순으로 정렬(default)
    @Override
    public List<PostDto> findAllNews(int offset, int limit) { // findAll query
        return jdbcTemplate.query("SELECT r.rname, r.category, p.* FROM post p\n" +
                "JOIN restaurant r ON r.restaurant_id = p.restaurant_id\n" +
                "WHERE p.type = 'NEWS' ORDER BY p.created_at DESC " +
                "LIMIT ? OFFSET ?;", newsRowMapper, limit, offset);
    }

    // view_count순으로 정렬
    @Override
    public List<PostDto> findAllNewsByViews(int offset, int limit) { // findAll query
        return jdbcTemplate.query("SELECT r.rname, r.category, p.* FROM post p\n" +
                "JOIN restaurant r ON r.restaurant_id = p.restaurant_id\n" +
                "WHERE p.type = 'NEWS' ORDER BY p.view_count DESC " +
                "LIMIT ? OFFSET ?;", newsRowMapper, limit, offset);
    }

    @Override
    public int countNews() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post WHERE type = 'NEWS'", Integer.class);
    }

    // template/news/detail.html에 매핑되는 sql 쿼리문(image 추가 필요)
    @Override
    public PostDto findNewsById(int id) {
        return jdbcTemplate.queryForObject("SELECT p.post_id, r.restaurant_id, r.rname, r.category, r.address, " +
                "r.phone, p.image, p.title, m.nickname, p.view_count, p.created_at, p.content, m.member_id " +
                "FROM post p JOIN member m ON p.member_id = m.member_id " +
                "JOIN restaurant r ON p.restaurant_id = r.restaurant_id " +
                "WHERE p.post_id = ? AND p.type = 'NEWS'", newsDetailMapper, id);
    }

    // 작성한 소식 insert
    @Override
    public void saveNews(PostDto post) {
        jdbcTemplate.update("INSERT INTO post(member_id, restaurant_id, type, image, title, content) VALUES (?, ?, ?, ?, ?, ?)",
                post.getMemberId(), post.getRestaurantId(), post.getType(), post.getImage(), post.getTitle(), post.getContent());
    }

    // manager member_id로 자신의 restaurant_id 조회
    public Optional<Integer> findRestaurantIdByMemberId(int memberId) {
        String sql = "SELECT restaurant_id FROM restaurant WHERE member_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("restaurant_id"), memberId).stream().findFirst();
    }
}
