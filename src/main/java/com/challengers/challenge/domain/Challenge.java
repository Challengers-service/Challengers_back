package com.challengers.challenge.domain;

import com.challengers.common.BaseTimeEntity;
import com.challengers.examplephoto.domain.ExamplePhoto;
import com.challengers.tag.domain.ChallengeTags;
import com.challengers.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseTimeEntity {
    @Setter @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    private String name;
    private String imageUrl;
    private String photoDescription;
    private String challengeRule;
    private int checkFrequencyDays;
    private int checkFrequencyTimes;
    private Category category;
    private LocalDate startDate;
    private LocalDate endDate;
    private int depositPoint;
    private String introduction;
    private Float totalStarRating;
    private Float starRating;
    private int reviewCount;
    private int userCount;
    private int userCountLimit;
    private ChallengeStatus status;

    @Embedded
    private ChallengeTags challengeTags = ChallengeTags.empty();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ExamplePhoto> examplePhotos = new ArrayList<>();


    public void addExamplePhotos(List<String> examplePhotoUrls) {
        for (String url : examplePhotoUrls) {
            ExamplePhoto examplePhoto = new ExamplePhoto(this, url);
            examplePhotos.add(examplePhoto);
            examplePhoto.setChallenge(this);
        }
    }

    public List<String> getExamplePhotoUrls() {
        return examplePhotos.stream()
                .map(ExamplePhoto::getPhoto_url)
                .collect(Collectors.toList());
    }

    @Builder
    public Challenge(Long id, User host, String name, String imageUrl, String photoDescription,
                     String challengeRule, int checkFrequencyDays, int checkFrequencyTimes, Category category,
                     LocalDate startDate, LocalDate endDate, int depositPoint, String introduction,
                     Float totalStarRating, Float starRating, int reviewCount, int userCount,
                     int userCountLimit, ChallengeStatus status) {
        this.id = id;
        this.host = host;
        this.name = name;
        this.imageUrl = imageUrl;
        this.photoDescription = photoDescription;
        this.challengeRule = challengeRule;
        this.checkFrequencyDays = checkFrequencyDays;
        this.checkFrequencyTimes = checkFrequencyTimes;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.introduction = introduction;
        this.totalStarRating = totalStarRating;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.userCount = userCount;
        this.userCountLimit = userCountLimit;
        this.status = status;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void joinUser() {
        this.userCount++;
    }

    public void addReviewRelation(Float starRating) {
        reviewCount++;
        totalStarRating += starRating;
        updateStarRating();
    }

    public void deleteReviewRelation(Float starRating) {
        reviewCount--;
        totalStarRating -= starRating;
        updateStarRating();
    }

    public void updateReviewRelation(Float starRating, Float newStarRating) {
        totalStarRating = totalStarRating - starRating + newStarRating;
        updateStarRating();
    }

    private void updateStarRating() {
        starRating = reviewCount == 0 ? 0.0f : Math.round(totalStarRating/reviewCount*10)/10.0f;
    }

}
