package net.likelion.bebc25.bytebite.member.repository;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;

import java.util.List;


public interface MemberRepository {

  // 회원 정보 저장
  int save(SignupDto signupDto);

  // email 회원 찾기
  MemberDto findByEmail(String email);

  // id 회원 찾기
  MemberDto findById(int id);

  // email 정보가 있는지 확인
  boolean existsByEmail(String email);

  // 회원 정보 수정
  void update(MemberDto member);

  // id 회원 삭제
  void deleteById(int id);

  // 회원 목록 조회
  List<MemberDto> findAll();
}
