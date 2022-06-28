package com.challengers.feed.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name="Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @NotNull
    @Column(name = "challenge_photo_id")
    private Long challengePhotoId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Builder
    public Like(Long id, Long challengePhotoId, Long userId) {
        this.id = id;
        this.challengePhotoId = challengePhotoId;
        this.userId = userId;
    }
}
