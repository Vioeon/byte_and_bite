package net.likelion.bebc25.bytebite.mypage.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;

public interface MypageRepository {
    MemberDto findProfileById(int member_id);
}
