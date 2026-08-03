//package net.likelion.bebc25.bytebite.news.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
//// 게시글 하나를 저장할 객체
//public class NewsDto {
//    private int newsId;
//    private int restaurantId;
//    private String restaurantName;
//    private String category;
//    private int views;
//
//    @NotBlank(message = "제목은 필수 입력 항목입니다.")
//    @Size(max = 200, message = "제목은 200자 이하로 입력해야 합니다.")
//    private String title;
//
//    @NotBlank(message = "내용은 필수 입력 항목입니다.")
//    private String content;
//
//    @NotBlank(message = "이미지는 필수로 첨부해야합니다.")
//    //@Size(max = 100, message = "작성자 이름은 2자 이상 10자 이하여야 합니다.")
//    private String image;
//
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//}