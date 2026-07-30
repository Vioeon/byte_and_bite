package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.exception.DuplicateEmailException;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;
import net.likelion.bebc25.bytebite.member.repository.MemberRepository;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void validateDuplicateEmail(SignupDto signupDto){
        boolean isDuplicate = memberRepository.existsByEmail(signupDto.getEmail());
        if(isDuplicate){
            throw new DuplicateEmailException("이미 사용중인 계정입니다.");
        }
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
    public MemberDto login(String email, String password) {
        MemberDto memberDto = memberRepository.findByEmail(email);
        if(memberDto != null && memberDto.getPassword().equals(password)){
            return memberDto;
        }
        return null;
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
