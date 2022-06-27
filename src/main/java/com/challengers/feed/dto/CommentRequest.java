package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    private String content;

    @Builder
    public CommentRequest(String content){
        this.content = content;
    }
}

