package net.likelion.bebc25.bytebite.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageDto<T> {

    private List<T> content; // 현재 페이지 데이터

    private int page; // 현재 페이지
    private int size; // 한 페이지 개수

    private int totalCount; // 전체 게시글 개수
    private int totalPage; // 전체 페이지 수


    public PageDto(List<T> content, int page, int size, int totalCount) {

        this.content = content;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        this.totalPage = (int)Math.ceil(
                (double) totalCount / size
        );
    }
}