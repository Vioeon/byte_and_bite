package net.likelion.bebc25.bytebite.member.dto;

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
    private String nickname;

    // 이메일
    private String email;

    // 비밀번호
    private String password;

    private String role;

    // 회원 가입 일시
    private LocalDateTime createdAt;
}
