package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;

import java.util.List;

public interface MemberService {

  // 회원가입
  void signup(SignupDto signupDto);

  // 로그인
  MemberDto login(String username, String password);

  // 회원정보 수정
  void modifyInfo(MemberDto member);

  // 회원 탈퇴
  void withdraw(int id);

  // 가입된 회원 목록
  List<MemberDto> getMembers();

  // 회원 조회 - id
  MemberDto getMember(int id);

  // 맛집 운영자 회원가입
  void signupWithRestaurant(SignupDto signupDto, RestaurantDto restaurantDto);
}
