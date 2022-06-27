package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengePhotoUserDto {
    private Long id;
    private String name;
    private String image;

    @Builder
    public ChallengePhotoUserDto(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}
