package com.challengers.challenge.domain;

import com.challengers.tag.domain.ChallengeTags;
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
    private LocalDateTime endDate;
    private int depositPoint;
    private boolean pointFix;
    private String introduction;
    private Float starRating;
    private int userCount;
    private ChallengeStatus status;

    @Embedded
    private ChallengeTags challengeTags = ChallengeTags.empty();

    @Builder
    public Challenge(Long id, User host, String name, String imageUrl,
                     String challengePhotoDescription, CheckFrequency checkFrequency, Category category,
                     LocalDateTime startDate, LocalDateTime endDate, int depositPoint, boolean pointFix, String introduction,
                     Float starRating, int userCount, ChallengeStatus status) {
        this.id = id;
        this.host = host;
        this.name = name;
        this.imageUrl = imageUrl;
        this.challengePhotoDescription = challengePhotoDescription;
        this.checkFrequency = checkFrequency;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.pointFix = pointFix;
        this.introduction = introduction;
        this.starRating = starRating;
        this.userCount = userCount;
        this.status = status;
    }

    public void setHost(User host) {
        this.host = host;
    }
}
