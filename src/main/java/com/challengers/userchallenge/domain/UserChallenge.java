package com.challengers.userchallenge.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.user.domain.User;
import com.challengers.userchallenge.ChallengeJoinManager;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "userChallenge")
    private List<PhotoCheck> photoChecks = new ArrayList<>();

    private int maxProgress;
    private int progress;
    private UserChallengeStatus status;

    @Builder
    public UserChallenge(Long id, Challenge challenge, User user, int maxProgress, int progress, UserChallengeStatus status) {
        this.id = id;
        this.challenge = challenge;
        this.maxProgress = maxProgress;
        this.progress = progress;
        this.user = user;
        this.status = status;
    }

    public static UserChallenge create(Challenge challenge, User user) {
        return UserChallenge.builder()
                .challenge(challenge)
                .user(user)
                .maxProgress(ChallengeJoinManager.getMaxProgress(challenge))
                .progress(0)
                .status(UserChallengeStatus.IN_PROGRESS)
                .build();
    }

    public void fail() {
        this.status = UserChallengeStatus.FAIL;
    }
}
