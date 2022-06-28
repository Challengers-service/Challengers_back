package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LikeResponse {
    private List<Long> likeList;

    @Builder
    public LikeResponse(List<Long> likeList) {
        this.likeList = likeList;
    }
}
