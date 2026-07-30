package net.likelion.bebc25.bytebite.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RestaurantDto {

    private Integer memberId;

    @NotNull(message = "식당 카테고리를 선택해주세요.")
    private String category;

    @NotBlank(message = "식당명은 필수 입력 항목입니다.")
    @Size(max = 50, message = "식당명은 50자 이하여야 합니다.")
    private String rname;

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phone;

    // DB 에 저장할 이미지 경로
    private String imageUrl;
    // 업로드 받을 이미지
    private MultipartFile image;

}
