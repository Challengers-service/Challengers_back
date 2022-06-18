package com.challengers.challenge.domain;

import com.challengers.examplephoto.domain.ExamplePhoto;
import com.challengers.tag.domain.ChallengeTags;
import com.challengers.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private String introduction;
    private Float starRating;
    private int userCount;
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
    public Challenge(Long id, User host, String name, String imageUrl,
                     String challengePhotoDescription, CheckFrequency checkFrequency, Category category,
                     LocalDateTime startDate, LocalDateTime endDate, int depositPoint, String introduction,
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
        this.introduction = introduction;
        this.starRating = starRating;
        this.userCount = userCount;
        this.status = status;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
