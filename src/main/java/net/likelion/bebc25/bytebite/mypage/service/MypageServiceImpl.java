package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.dto.mypagePostDto;
import net.likelion.bebc25.bytebite.mypage.repository.MypageRepository;
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
    // id 에 해당하는 일반 사용자가 작성한 리뷰 목록 조회
    @Override
    public List<mypagePostDto> getPostListById(int memberId) {
        return mypageRepository.findPostListById(memberId);
    }

    // id 에 해당하는 맛집 운영자가 작성한 리뷰 소식 조회
    @Override
    public List<mypagePostDto> getNewsListById(int memberId) {
        return mypageRepository.findNewsListById(memberId);
    }

}
