package net.likelion.bebc25.bytebite.reply.dto;

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

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}