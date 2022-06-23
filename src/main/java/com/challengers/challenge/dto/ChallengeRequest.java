package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequency;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeRequest {
    @NotBlank
    private String challengeName;
    private MultipartFile image;
    @NotBlank
    private String challengeRule;
    @NotNull
    private String checkFrequency;
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
    private List<MultipartFile> examplePhotos;
    private List<@NotBlank String> tags;

    @Builder
    public ChallengeRequest(String challengeName, MultipartFile image, String challengeRule,
                            String checkFrequency, String category, LocalDate startDate, LocalDate endDate,
                            int depositPoint, String introduction, List<MultipartFile> examplePhotos, List<String> tags) {
        this.challengeName = challengeName;
        this.image = image;
        this.challengeRule = challengeRule;
        this.checkFrequency = checkFrequency;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.introduction = introduction;
        this.examplePhotos = examplePhotos;
        this.tags = tags;
    }

    public Challenge toChallenge() {
        return Challenge.builder()
                .name(challengeName)
                .challengeRule(challengeRule)
                .checkFrequency(CheckFrequency.of(checkFrequency))
                .category(Category.of(category))
                .startDate(startDate)
                .endDate(endDate)
                .depositPoint(depositPoint)
                .introduction(introduction)
                .build();
    }
}
