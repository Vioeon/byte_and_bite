package net.likelion.bebc25.bytebite.reply.controller;

import jakarta.servlet.http.HttpSession;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.reply.dto.ReplyDto;
import net.likelion.bebc25.bytebite.reply.service.ReplyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    // 댓글 등록
    @PostMapping("/write")
    public String writeReply(@PathVariable int postId,
                             @ModelAttribute ReplyDto reply,
                             HttpSession session) {

        // 로그인한 회원 정보 조회
        SessionMemberDto loginMember =
                (SessionMemberDto) session.getAttribute("loginMember");

        // 로그인하지 않은 경우 로그인 페이지로 이동
        if (loginMember == null) {
            return "redirect:/member/login";
        }

        // 게시글 번호 설정
        reply.setPostId(postId);

        // 로그인한 회원 번호 설정
        reply.setMemberId(loginMember.getMemberId());

        // 댓글 저장
        replyService.writeReply(reply);

        // 게시글 상세페이지로 이동
        return "redirect:/posts/" + postId;
    }

    // 댓글 수정
    @PostMapping("/{replyId}/edit")
    public String updateReply(@PathVariable int postId,
                              @PathVariable int replyId,
                              @ModelAttribute ReplyDto reply,
                              HttpSession session) {

        // 로그인한 회원 정보 조회
        SessionMemberDto loginMember =
                (SessionMemberDto) session.getAttribute("loginMember");

        // 로그인하지 않은 경우 로그인 페이지로 이동
        if (loginMember == null) {
            return "redirect:/member/login";
        }

        // 게시글 번호 설정
        reply.setPostId(postId);

        // 댓글 번호 설정
        reply.setReplyId(replyId);

        // 로그인한 회원 번호 설정
        reply.setMemberId(loginMember.getMemberId());

        // 댓글 수정
        replyService.updateReply(reply);

        // 게시글 상세페이지로 이동
        return "redirect:/posts/" + postId;
    }
}