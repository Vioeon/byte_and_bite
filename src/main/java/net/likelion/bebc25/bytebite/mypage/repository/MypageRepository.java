package net.likelion.bebc25.bytebite.mypage.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.mypage.dto.mypagePostDto;

import java.util.List;

public interface MypageRepository {

    // id 에 해당하는 회원정보 찾기
    MemberDto findProfileById(int memberId);

    // id 에 해당하는 일반 사용자가 작성한 리뷰 목록 찾기
    List<mypagePostDto> findPostListById(int memberId);

    // id 에 해당하는 맛집 운영자가 작성한 소식 목록 찾기
    List<mypagePostDto> findNewsListById(int memberId);

}
