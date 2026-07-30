package net.likelion.bebc25.bytebite.member.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SessionMemberDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // 회원 번호
    private int memberId;

    // 닉네임
    private String nickname;

    // email
    private String email;

    // USER, MANAGER
    private String role;

    // MemberDto 객체로부터 세션 DTO를 생성
    public SessionMemberDto(MemberDto memberDto) {
        if (memberDto != null) {
            this.memberId = memberDto.getMemberId();
            this.nickname = memberDto.getNickname();
            this.email = memberDto.getEmail();
            this.role = memberDto.getRole();
        }
    }
}
