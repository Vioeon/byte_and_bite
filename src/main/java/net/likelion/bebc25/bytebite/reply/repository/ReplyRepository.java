package net.likelion.bebc25.bytebite.reply.repository;

import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;

import java.util.List;

public interface ReplyRepository {

    List<ReplyDto> findByPostId(int postId);

}