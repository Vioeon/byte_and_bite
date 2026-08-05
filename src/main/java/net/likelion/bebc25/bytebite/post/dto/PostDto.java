package net.likelion.bebc25.bytebite.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
    private int resMemberId;
    private String restaurantName;
    private String restaurantImage;
    private String address;
    private String phone;

    private String type;
    private String category;
    // DB에 저정되어있는 카테고리명을 화면 표시에서 한글로 변환해서 출력
    public String categoryName() {

        if (category == null) {
            return "";
        }

        // 향상된 switch 문
        return switch (category) {
            case "KOR" -> "한식";
            case "WST" -> "양식";
            case "CHN" -> "중식";
            case "JPN" -> "일식";
            case "ETC" -> "기타";
            // 정해지지 않은 값이 들어오면 원래 값으로 반환
            default -> category;
        };
    }

    private String role;

    private String image;
    @NotNull
    private MultipartFile[] images;

    private String newsImage;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    private int views;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean canWrite(SessionMemberDto member) {
        if (member == null) {
            return false;
        }

        return member.getRole().equals("USER") || member.getMemberId() == resMemberId;
    }

}