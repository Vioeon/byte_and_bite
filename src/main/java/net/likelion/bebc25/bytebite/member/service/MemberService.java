package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;

public interface MemberService {

  // email 중복 체크
  void validateDuplicateEmail(SignupDto signupDto);
  // 닉네임 중복 체크
  void validateDuplicateNickname(SignupDto signupDto);

  // 회원가입
  void signup(SignupDto signupDto);

  // 로그인
  MemberDto login(String username, String password);

  // 회원 탈퇴
  void withdraw(int id);

  // 맛집 운영자 회원가입
  void signupWithRestaurant(SignupDto signupDto, RestaurantDto restaurantDto);

  void updateNickname(int memberId, String newNickname);
}
