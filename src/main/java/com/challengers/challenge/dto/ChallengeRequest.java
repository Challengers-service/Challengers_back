package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequency;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeRequest {
    private String challengeName;
    private String imageUrl;
    private String challengePhotoDescription;
    private String checkFrequency;
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private int depositPoint;
    private boolean pointFix;
    private String introduction;
    private List<String> goodExamplePhotoUrls;
    private List<String> badExamplePhotoUrls;
    private List<String> tags;

    @Builder
    public ChallengeRequest(String challengeName, String imageUrl, String challengePhotoDescription,
                            String checkFrequency, String category, LocalDateTime startDate, LocalDateTime endDate,
                            int depositPoint, boolean pointFix, String introduction, List<String> goodExamplePhotoUrls,
                            List<String> badExamplePhotoUrls, List<String> tags) {
        this.challengeName = challengeName;
        this.imageUrl = imageUrl;
        this.challengePhotoDescription = challengePhotoDescription;
        this.checkFrequency = checkFrequency;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.pointFix = pointFix;
        this.introduction = introduction;
        this.goodExamplePhotoUrls = goodExamplePhotoUrls;
        this.badExamplePhotoUrls = badExamplePhotoUrls;
        this.tags = tags;
    }

    public Challenge toChallenge() {
        return Challenge.builder()
                .name(challengeName)
                .imageUrl(imageUrl)
                .challengePhotoDescription(challengePhotoDescription)
                .checkFrequency(CheckFrequency.of(checkFrequency))
                .category(Category.of(category))
                .startDate(startDate)
                .endDate(endDate)
                .depositPoint(depositPoint)
                .pointFix(pointFix)
                .introduction(introduction)
                .build();
    }

}
