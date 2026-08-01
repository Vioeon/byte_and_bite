package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;

public interface MypageService {
    MemberDto getProfileById(int member_id);
}
