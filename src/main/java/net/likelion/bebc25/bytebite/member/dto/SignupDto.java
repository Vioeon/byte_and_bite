package net.likelion.bebc25.bytebite.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SignupDto {

    // 이메일
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Size(min = 4, max = 50, message = "이메일은 4자 이상 50자 이하여야 합니다.")
    private String email;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    // 비밀번호 확인
    @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String passwordConfirm;

    // 닉네임
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "닉네임 2자 이상 20자 이하여야 합니다.")
    private String nickname;

    // 기본값 USER
    private String role = "USER";
}