package net.likelion.bebc25.bytebite.news.repository;

import net.likelion.bebc25.bytebite.news.dto.NewsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PureJdbcNewsRepository implements NewsRepository {

    @Value("${spring.datasource.url}")
    private String url;
    //private String url = "jdbc:mysql://localhost:3306/bytebite?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    // 소식 목록 조회
    @Override
    public List<NewsDto> findAll() {
        String sql = "SELECT title, created_at AS createdAt FROM news";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        List<NewsDto> result = new ArrayList<>();

        try{ // 플랜 A
            // 1. 데이터베이스 연결(Connection 객체 생성)
            conn = DriverManager.getConnection(url, user, password);

            // 2. SQL 실행 객체 생성(Statement 객체 생성)
            stmt = conn.createStatement();

            // 3. SQL 실행
            rs = stmt.executeQuery(sql);

            // 4. 결과 처리(ResultSet 사용)
            while(rs.next()){
                NewsDto newsDto = new NewsDto();
                //postDto.setId(rs.getInt("id"));
                newsDto.setTitle(rs.getString("title"));
                //postDto.setAuthor(rs.getString("author"));
                newsDto.setCreatedAt(rs.getObject("createdAt", LocalDateTime.class));

                result.add(newsDto);
            }

        }catch(Exception e){ // 플랜 B
            System.out.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
        }finally{
            // 5. 생성된 리소스를 생성의 역순으로 해제
            try{ if(rs != null) rs.close(); } catch (Exception e){ }
            try{ if(stmt != null) stmt.close(); } catch (Exception e){ }
            try{ if(conn != null) conn.close(); } catch (Exception e){ }
        }

        return result;
    }
}
