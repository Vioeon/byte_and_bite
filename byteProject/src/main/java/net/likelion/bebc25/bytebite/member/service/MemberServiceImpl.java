package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;
import net.likelion.bebc25.bytebite.member.repository.MemberRepository;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileService fileService;

    public MemberServiceImpl(@Qualifier("jdbcMemberRepository") MemberRepository memberRepository, RestaurantRepository restaurantRepository, FileService fileService) {
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
        this.fileService = fileService;
    }

    // 회원가입
    @Override
    public void signup(SignupDto signupDto) {

        memberRepository.save(signupDto);
    }

    @Transactional
    public void signupWithRestaurant(SignupDto signupDto, RestaurantDto restaurantDto) {
        int memberId = memberRepository.save(signupDto);

        String imagePath = fileService.save(restaurantDto.getImage());

        restaurantDto.setMemberId(memberId);
        restaurantDto.setImageUrl(imagePath);
        restaurantRepository.save(restaurantDto);
    }

    // 로그인
    @Override
    public MemberDto login(String username, String password) {

        return memberRepository.findByUsername(username);
    }

    // 회원정보 수정
    @Override
    public void modifyInfo(MemberDto member) {

        memberRepository.update(member);
    }

    // 탈퇴
    @Override
    public void withdraw(int id) {

        memberRepository.deleteById(id);
    }

    // 회원 목록 조회
    @Override
    public List<MemberDto> getMembers() {
        return memberRepository.findAll();
    }

    // 회원 조회 - id
    @Override
    public MemberDto getMember(int id) {
        return memberRepository.findById(id);
    }
}
