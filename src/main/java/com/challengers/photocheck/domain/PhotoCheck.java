package com.challengers.photocheck.domain;

import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.userchallenge.domain.UserChallenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PhotoCheck {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_check_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_challenge_id")
    private UserChallenge userChallenge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_photo_id")
    private ChallengePhoto challengePhoto;

    private int round;

    @Enumerated(EnumType.STRING)
    private PhotoCheckStatus status;

    @Builder
    public PhotoCheck(Long id, UserChallenge userChallenge, ChallengePhoto challengePhoto,
                      int round, PhotoCheckStatus status) {
        this.id = id;
        this.userChallenge = userChallenge;
        this.challengePhoto = challengePhoto;
        this.round = round;
        this.status = status;
    }

    public void pass() {
        status = PhotoCheckStatus.PASS;
    }

    public void fail() {
        status = PhotoCheckStatus.FAIL;
    }
}
