package com.challengers.follow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowResponse {
    Long id;
    String name;
    String image;

    @Builder
    public FollowResponse(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}