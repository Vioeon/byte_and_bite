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

    @Override
    public MemberDto getProfileById(int memberId) {

        return mypageRepository.findProfileById(memberId);
    }
}
