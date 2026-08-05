package net.likelion.bebc25.bytebite.mypage.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.dto.mypagePostDto;

import java.util.List;

public interface MypageService {

    // id 에 해당하는 회원정보 조회
    MemberDto getProfileById(int memberId);

    // id 에 해당하는 일반 사용자가 작성한 리뷰 목록 조회
    List<mypagePostDto> getPostListById(int memberId);

    // id 에 해당하는 맛집 운영자가 작성한 소식 목록 조회
    List<mypagePostDto> getNewsListById(int memberId);

}
