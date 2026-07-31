package net.likelion.bebc25.bytebite.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
    private String email;

    // 비밀번호
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,20}$",
            message = "영문, 숫자, 특수문자를 포함하여 8~20자로 입력해주세요."
    )
    private String password;

    // 비밀번호 확인
    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    // 닉네임
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임 2~10자 이하여야 합니다.")
    private String nickname;

    // 기본값 USER
    private String role = "USER";
}