package com.challengers.feed;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @Column(name = "challenge_photo_id")
    private Long challengePhotoId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public Comment(Long id, Long userId, Long challengePhotoId, String content) {
        this.id = id;
        this.userId = userId;
        this.challengePhotoId = challengePhotoId;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
