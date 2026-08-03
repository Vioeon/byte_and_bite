//package net.likelion.bebc25.bytebite.news.repository;
//
//import net.likelion.bebc25.bytebite.news.dto.NewsDto;
//import net.likelion.bebc25.bytebite.post.dto.PostDto;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public class JdbcTemplateNewsRepository implements NewsRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public JdbcTemplateNewsRepository(JdbcTemplate jdbcTemplate){
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    private final RowMapper<NewsDto> newsRowMapper = (ResultSet rs, int rowNum) -> {
//        return NewsDto.builder()
//                .newsId(rs.getInt("post_id"))
//                .restaurantId(rs.getInt("restaurant_id"))
//                .restaurantName(rs.getString("rname"))
//                .category(rs.getString("category"))
//                .title(rs.getString("title"))
//                .views(rs.getInt("view_count"))
//                .image(rs.getString("image"))
//                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
//    };
//
////    private final RowMapper<NewsDto> newsDetailMapper = (ResultSet rs, int rowNum) -> {
////        return NewsDto.builder()
////                //.id(rs.getInt("id"))
////                .title(rs.getString("title"))
////                //.author(rs.getString("author"))
////                .createdAt(rs.getObject("created_at", LocalDateTime.class)).build();
////                //.content(rs.getString("content")).build();
////    };
//
//    // 작성일시순으로 정렬(default)
//    @Override
//    public List<NewsDto> findAll() { // findAll query
//        return jdbcTemplate.query("SELECT r.rname, r.category, p.* FROM post p\n" +
//                "JOIN restaurant r ON r.restaurant_id = p.restaurant_id\n" +
//                "WHERE p.type = 'NEWS' " +
//                "ORDER BY p.created_at DESC;", newsRowMapper);
//    }
//
//    // view_count순으로 정렬
//    @Override
//    public List<NewsDto> findAllByViews() { // findAll query
//        return jdbcTemplate.query("SELECT r.rname, r.category, p.* FROM post p\n" +
//                "JOIN restaurant r ON r.restaurant_id = p.restaurant_id\n" +
//                "WHERE p.type = 'NEWS' " +
//                "ORDER BY p.view_count DESC;", newsRowMapper);
//    }
//}
