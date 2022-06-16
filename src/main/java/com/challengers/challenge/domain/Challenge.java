package com.challengers.challenge.domain;

import com.challengers.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
    @Setter @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    private String name;
    private String imageUrl;
    private String challengePhotoDescription;
    private CheckFrequency checkFrequency;
    private Category category;
    private LocalDateTime startDate;
    private int period;
    private int depositPoint;
    private boolean pointFix;
    private String introduction;
    private Float starRating;
    private int userCount;
    private ChallengeStatus status;

    @Builder
    public Challenge(Long id, User host, String name, String imageUrl,
                     String challengePhotoDescription, CheckFrequency checkFrequency, Category category,
                     LocalDateTime startDate, int period, int depositPoint, boolean pointFix, String introduction,
                     Float starRating, int userCount, ChallengeStatus status) {
        this.id = id;
        this.host = host;
        this.name = name;
        this.imageUrl = imageUrl;
        this.challengePhotoDescription = challengePhotoDescription;
        this.checkFrequency = checkFrequency;
        this.category = category;
        this.startDate = startDate;
        this.period = period;
        this.depositPoint = depositPoint;
        this.pointFix = pointFix;
        this.introduction = introduction;
        this.starRating = starRating;
        this.userCount = userCount;
        this.status = status;
    }
}
