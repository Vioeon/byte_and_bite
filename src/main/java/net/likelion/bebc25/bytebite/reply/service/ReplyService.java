package net.likelion.bebc25.bytebite.reply.service;

import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

    List<ReplyDto> findByPostId(int postId, int page);

    int countByPostId(int postId);

    void save(ReplyDto reply);

}