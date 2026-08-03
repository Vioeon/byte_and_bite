package net.likelion.bebc25.bytebite.reply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDto {

    private int replyId;
    private int postId;
    private int memberId;

    // 댓글 작성자 닉네임
    private String nickname;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(max = 300, message = "댓글은 300자 이하로 입력해주세요.")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}