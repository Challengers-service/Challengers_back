package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {
    private Long id;
    private ChallengePhotoUserDto auth;
    private String content;

    @Builder
    public CommentDto(Long id, ChallengePhotoUserDto auth, String content) {
        this.id = id;
        this.auth = auth;
        this.content = content;
    }
}
