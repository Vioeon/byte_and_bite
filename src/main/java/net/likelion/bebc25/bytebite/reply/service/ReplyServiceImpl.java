package net.likelion.bebc25.bytebite.reply.service;

import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;
import net.likelion.bebc25.bytebite.reply.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public List<ReplyDto> findByPostId(int postId) {
        return replyRepository.findByPostId(postId);
    }
}