package net.likelion.bebc25.bytebite.mypage.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class mypagePostDto {

private int id;

private String imageUrl;

private String title;

private String restaurantName;

private LocalDateTime createdAt;

}
