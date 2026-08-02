package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.repository.MypageRepository;
import org.springframework.stereotype.Service;

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
}
