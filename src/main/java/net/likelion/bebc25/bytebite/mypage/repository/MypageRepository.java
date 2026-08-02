package net.likelion.bebc25.bytebite.mypage.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;

public interface MypageRepository {

    // id 에 해당하는 회원정보 찾기
    MemberDto findProfileById(int memberId);
}
