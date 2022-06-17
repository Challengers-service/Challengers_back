package com.challengers.userchallenge.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isFinished;

    public UserChallenge(Challenge challenge, User user, boolean isFinished) {
        this.challenge = challenge;
        this.user = user;
        this.isFinished = isFinished;
    }
}
