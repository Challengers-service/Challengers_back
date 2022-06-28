package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private ChallengePhotoUserDto auth;
    private String title;
    private String image;
    private Long commentCnt;
    private Long likeCnt;

    @Builder
    public PostResponse(Long id, ChallengePhotoUserDto ChallengePhotoUserDto, String title, String image, Long commentCnt, Long likeCnt) {
        this.id = id;
        this.auth = ChallengePhotoUserDto;
        this.title = title;
        this.image = image;
        this.commentCnt = commentCnt;
        this.likeCnt = likeCnt;
    }
}
