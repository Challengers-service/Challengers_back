package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeRequest {
    @NotBlank
    private String name;

    private MultipartFile image;
    @NotBlank
    private String photoDescription;
    @NotBlank
    private String challengeRule;
    @NotNull
    private String checkFrequencyType;

    private int checkTimesPerRound;
    @NotNull
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate endDate;
    private int depositPoint;
    @NotBlank
    private String introduction;
    @NotNull
    private int userCountLimit;
    @NotNull
    private List<MultipartFile> examplePhotos;
    private List<@NotBlank String> tags;

    @Builder
    public ChallengeRequest(String name, MultipartFile image, String photoDescription, String challengeRule,
                            String checkFrequencyType, int checkTimesPerRound, String category, LocalDate startDate,
                            LocalDate endDate, int depositPoint, String introduction, int userCountLimit,
                            List<MultipartFile> examplePhotos, List<String> tags) {
        this.name = name;
        this.image = image;
        this.photoDescription = photoDescription;
        this.challengeRule = challengeRule;
        this.checkFrequencyType = checkFrequencyType;
        this.checkTimesPerRound = checkTimesPerRound;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.introduction = introduction;
        this.userCountLimit = userCountLimit;
        this.examplePhotos = examplePhotos;
        this.tags = tags;
    }

    public Challenge toChallenge() {
        return Challenge.builder()
                .name(name)
                .photoDescription(photoDescription)
                .challengeRule(challengeRule)
                .checkFrequencyType(CheckFrequencyType.of(checkFrequencyType))
                .checkTimesPerRound(checkTimesPerRound)
                .category(Category.of(category))
                .startDate(startDate)
                .endDate(endDate)
                .depositPoint(depositPoint)
                .introduction(introduction)
                .userCountLimit(userCountLimit)
                .totalStarRating(0.0f)
                .starRating(0.0f)
                .reviewCount(0)
                .userCount(1)
                .round(0)
                .build();
    }
}
