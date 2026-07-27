package net.likelion.bebc25.example.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LoginDto {

    // 회원 아이디
    private int id;

    // 로그인 아이디
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 4, max = 50, message = "아이디는 4자 이상 50자 이하여야 합니다.")
    private String username;

    // 로그인 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하여야 합니다.")
    private String password;
}
