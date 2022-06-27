package com.challengers.userchallenge.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.user.domain.User;
import com.challengers.userchallenge.ChallengeJoinManager;
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

    private int maxProgress;
    private int progress;
    private UserChallengeStatus status;

    public UserChallenge(Challenge challenge, User user, int maxProgress, int progress, UserChallengeStatus status) {
        this.challenge = challenge;
        this.maxProgress = maxProgress;
        this.progress = progress;
        this.user = user;
        this.status = status;
    }

    public static UserChallenge create(Challenge challenge, User user) {
        return new UserChallenge(
                challenge,
                user,
                ChallengeJoinManager.getMaxProgress(challenge),
                0,
                UserChallengeStatus.IN_PROGRESS
        );
    }
}
