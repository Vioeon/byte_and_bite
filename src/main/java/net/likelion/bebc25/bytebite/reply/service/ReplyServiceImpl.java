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

    // 댓글 조회
    @Override
    public List<ReplyDto> findByPostId(int postId, int page) {
        return replyRepository.findByPostId(postId, page);
    }

    @Override
    public int countByPostId(int postId) {
        return replyRepository.countByPostId(postId);
    }

    // 댓글 등록
    @Override
    public void save(ReplyDto reply) {
        replyRepository.save(reply);
    }
}