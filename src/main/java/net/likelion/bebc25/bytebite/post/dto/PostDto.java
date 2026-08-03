package net.likelion.bebc25.bytebite.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostDto {
    private int id;

    private int memberId;
    private String nickname;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해야 합니다.")
    private String title;

//    @NotNull(message = "식당을 선택해주세요.")
    private int restaurantId;
    private String restaurantName;

    private String category;

    private String type; // "POST", "NEWS"

    private String image;
    private MultipartFile images;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    private int views;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}