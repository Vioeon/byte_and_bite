package net.likelion.bebc25.bytebite.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 정보 데이터를 전달하기 위한 데이터 객체(DTO)입니다.
 * 유효성 검증을 위한 어노테이션이 적용되어 있습니다.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MemberDto {
    // id
    private int memberId;

    // 닉네임
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "닉네임 2자 이상 20자 이하여야 합니다.")
    private String nickname;

    // 이메일
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Size(min = 4, max = 50, message = "이메일은 4자 이상 50자 이하여야 합니다.")
    private String email;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    private String role;

    // 회원 가입 일시
    private LocalDateTime createdAt;
}
