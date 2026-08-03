package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.repository.MypageRepository;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MypageServiceImpl implements MypageService {

    private final MypageRepository mypageRepository;

    public MypageServiceImpl(MypageRepository mypageRepository) {

        this.mypageRepository = mypageRepository;
    }

    // id 에 해당하는 회원정보 조회
    @Override
    public MemberDto getProfileById(int memberId) {

        return mypageRepository.findProfileById(memberId);
    }

    @Override
    public List<PostDto> getPostListById(int memberId) {
        return mypageRepository.findPostListById(memberId);
    }

}
