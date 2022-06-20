package com.challengers.review.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.common.BaseTimeEntity;
import com.challengers.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private String title;
    private String content;
    private Float starRating;

    @Builder
    public Review(Long id, User user, Challenge challenge, String title, String content, Float starRating) {
        this.id = id;
        this.user = user;
        this.challenge = challenge;
        this.title = title;
        this.content = content;
        this.starRating = starRating;
    }
}
