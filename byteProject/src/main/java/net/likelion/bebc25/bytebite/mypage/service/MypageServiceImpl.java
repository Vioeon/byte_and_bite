//package net.likelion.bebc25.bytebite.mypage.service;
//
//import net.likelion.bebc25.bytebite.member.dto.MemberDto;
//import net.likelion.bebc25.bytebite.mypage.repository.MypageRepository;
//import net.likelion.bebc25.bytebite.post.dto.PostDto;
//import net.likelion.bebc25.bytebite.post.repository.PostRepository;
//import net.likelion.bebc25.bytebite.post.service.PostService;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MypageServiceImpl implements MypageService {
//
//    private final MypageRepository mypageRepository;
//
//    public MypageServiceImpl(@Qualifier("jdbcTemplateMypageRepository") MypageRepository mypageRepository){
//        this.mypageRepository = mypageRepository;
//    }
//
//    @Override
//    public List<MemberDto> getmembers () {
//        return mypageRepository.findAll();
//    }
//
//    @Override
//    public PostDto getmember(int id) {
//        return mypageRepository.findById(id);
//    }
//
//    @Override
//    public void writePost(PostDto post) {
//        mypageRepository.save(post);
//    }
//
//    @Override
//    public void update(MemberDto member) { mypageRepository.update(member);
//    }
//
//    @Override
//    public void deleteById(int id) { mypageRepository.deleteById(id);
//    }
//}
