package com.challengers.challengephoto.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChallengePhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String photoUrl;

    @Builder
    public ChallengePhoto(Long id, Challenge challenge, User user, String photoUrl) {
        this.id = id;
        this.challenge = challenge;
        this.user = user;
        this.photoUrl = photoUrl;
    }

    public static ChallengePhoto create(Challenge challenge, User user, String photoUrl) {
        return ChallengePhoto.builder()
                .challenge(challenge)
                .user(user)
                .photoUrl(photoUrl)
                .build();
    }
}
