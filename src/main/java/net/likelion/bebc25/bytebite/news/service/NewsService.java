package net.likelion.bebc25.bytebite.news.service;

import net.likelion.bebc25.bytebite.member.dto.MemberDto;
import net.likelion.bebc25.bytebite.member.dto.SessionMemberDto;
import net.likelion.bebc25.bytebite.post.dto.PostDto;
import net.likelion.bebc25.bytebite.post.dto.NewPageDto;

import java.util.List;

public interface NewsService {
    NewPageDto<PostDto> getNewsList(int page, int size, String sort); // 정렬기준 sort로 전달

    PostDto getNews(int id); // get
    void writeNews(PostDto post, SessionMemberDto loginMember); // posts
//    void editNews(NewsDto posts); // posts
//    void removeNews(int id); // posts
}