package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MemberService 인터페이스의 비즈니스 로직을 처리하는 기본 구현 클래스입니다.
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * 생성자를 통해 MemberRepository 의존성을 주입받습니다.
     *
     * @param memberRepository 주입받을 MemberRepository 스프링 빈 객체
     */
    public MemberServiceImpl(@Qualifier("jdbcTemplateMemberRepository") MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * {@inheritDoc} 회원가입
     */
    @Override
    public void register(MemberDto member) {
        // 실습 영역
        memberRepository.save(member);
    }

    /**
     * {@inheritDoc} 로그인
     */
    @Override
    public MemberDto login(String username, String password) {
        // 실습 영역
        return memberRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc} 회원정보 수정
     */
    @Override
    public void modifyInfo(MemberDto member) {
        // 실습 영역
        memberRepository.update(member);
    }

    /**
     * {@inheritDoc} 탈퇴
     */
    @Override
    public void withdraw(int id) {
        // 실습 영역
        memberRepository.deleteById(id);
    }

    /**
     * {@inheritDoc} 회원 조회
     */
    @Override
    public List<MemberDto> getMembers() {
        return memberRepository.findAll();
    }

    /**
     * {@inheritDoc} 회원 id로 조회
     */
    @Override
    public MemberDto getMember(int id) {
        return memberRepository.findById(id);
    }
}
