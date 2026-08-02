package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;

public interface MypageService {

    // id 에 해당하는 회원정보 조회
    MemberDto getProfileById(int memberId);
}
