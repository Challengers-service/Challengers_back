package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChallengeResponse {
    private Long challengeId;
    private String name;
    private String category;
    private List<String> tags;
    private String createdDate;
    private int remainingDays;
    private boolean cart;
    private boolean hasJoined;
    private List<String> profileImgUrls;

    public ChallengeResponse(Challenge challenge, boolean cart, boolean hasJoined, List<String> profileImgUrls) {
        challengeId = challenge.getId();
        name = challenge.getName();
        category = challenge.getCategory().toString();
        tags = challenge.getChallengeTags().getStringTags();
        createdDate = challenge.getCreatedDateYYYYMMDD();
        remainingDays = (int) ChronoUnit.DAYS.between(LocalDate.now(),challenge.getEndDate());
        this.cart = cart;
        this.hasJoined = hasJoined;
        this.profileImgUrls = profileImgUrls;
    }
}
