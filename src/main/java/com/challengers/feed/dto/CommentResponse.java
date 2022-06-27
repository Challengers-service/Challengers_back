package com.challengers.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private List<CommentDto> comments;
    private Long commentCnt;

    @Builder
    public CommentResponse(List<CommentDto> comments, Long commentCnt){
        this.comments = comments;
        this.commentCnt = commentCnt;
    }
}
