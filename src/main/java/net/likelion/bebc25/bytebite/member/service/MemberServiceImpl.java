package net.likelion.bebc25.bytebite.member.service;

import net.likelion.bebc25.bytebite.exception.DuplicateEmailException;
import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.RestaurantDto;
import net.likelion.bebc25.bytebite.member.dto.SignupDto;
import net.likelion.bebc25.bytebite.member.repository.MemberRepository;
import net.likelion.bebc25.bytebite.member.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    @Override
    public void validateDuplicateEmail(SignupDto signupDto) {
        boolean isDuplicate = memberRepository.existsByEmail(signupDto.getEmail());
        if (isDuplicate) {
            throw new DuplicateEmailException("이미 사용중인 계정입니다.");
        }
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 회원가입
    @Override
    public void signup(SignupDto signupDto) {
        memberRepository.save(signupDto);
    }

    @Transactional
    public void signupWithRestaurant(SignupDto signupDto, RestaurantDto restaurantDto) {
        int memberId = memberRepository.save(signupDto);

        restaurantDto.setMemberId(memberId);
        MultipartFile imageFile = restaurantDto.getImage();

        try {
            String originalName = imageFile.getOriginalFilename();
            String savedName = UUID.randomUUID() + originalName;

            Path directory = Paths.get(uploadDir, "restaurant");
            Files.createDirectories(directory);
            imageFile.transferTo(directory.resolve(savedName));

            restaurantDto.setImageUrl("/uploads/restaurant/" + savedName);
            restaurantRepository.save(restaurantDto);

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }
    }

    // 로그인
    @Override
    public MemberDto login(String email, String password) {
        MemberDto memberDto = memberRepository.findByEmail(email);
        if (memberDto != null && memberDto.getPassword().equals(password)) {
            return memberDto;
        }
        return null;
    }

    // 탈퇴
    @Override
    public void withdraw(int id) {
        memberRepository.withdrawUser(id);
    }
}
