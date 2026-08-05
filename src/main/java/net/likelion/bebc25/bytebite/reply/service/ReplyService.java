package net.likelion.bebc25.bytebite.reply.service;

import net.likelion.bebc25.bytebite.post.dto.NewPageDto;
import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;

public interface ReplyService {

    NewPageDto<ReplyDto> findByPostId(int postId, int page, int size);

    int countByPostId(int postId);

    void writeReply(ReplyDto reply);

    void updateReply(ReplyDto reply);

    void deleteReply(int replyId, int memberId);
}