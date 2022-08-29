package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 2000)
    private String challengeRule;
    @NotNull
    private String checkFrequencyType;
    @NotNull
    @Range(min = 1, max = 7)
    private int checkTimesPerRound;
    @NotNull
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @NotNull
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Range(max = 10000L)
    private int depositPoint;
    @NotBlank
    @Size(min = 1, max = 2000)
    private String introduction;
    @NotNull
    @Range(min = 1L, max = 1000L)
    private int userCountLimit;
    @NotEmpty
    @Size(min = 1, max = 3)
    private List<MultipartFile> examplePhotos;
    @Size(max = 10)
    private List<@NotBlank String> tags;

    @Builder
    public ChallengeRequest(String name, String challengeRule, String checkFrequencyType, int checkTimesPerRound,
                            String category, LocalDate startDate, LocalDate endDate, int depositPoint,
                            String introduction, int userCountLimit, List<MultipartFile> examplePhotos,
                            List<String> tags) {
        this.name = name;
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
                .challengeRule(challengeRule)
                .checkFrequencyType(CheckFrequencyType.of(checkFrequencyType))
                .checkTimesPerRound(checkTimesPerRound)
                .category(Category.of(category))
                .startDate(startDate)
                .endDate(endDate)
                .depositPoint(depositPoint)
                .introduction(introduction)
                .userCountLimit(userCountLimit)
                .round(0)
                .build();
    }
}
