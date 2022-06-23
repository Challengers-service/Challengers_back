package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.tag.dto.TagResponse;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeDetailResponse {
    private Long id;

    private Long hostId;
    private String hostProfileImageUrl;
    private String hostName;

    private String name;
    private String imageUrl;
    private String challengeRule;
    private String checkFrequency;
    private String category;
    private String startDate;
    private String endDate;
    private int depositPoint;
    private Float starRating;
    private Long userCount;
    private String status;
    private List<TagResponse> tags;
    private List<String> examplePhotos;

    public static ChallengeDetailResponse of(Challenge challenge, Long userCount) {
        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getId(),
                challenge.getHost().getImage(),
                challenge.getHost().getName(),
                challenge.getName(),
                challenge.getImageUrl(),
                challenge.getChallengeRule(),
                challenge.getCheckFrequency().toString(),
                challenge.getCategory().toString(),
                challenge.getStartDate().toString(),
                challenge.getEndDate().toString(),
                challenge.getDepositPoint(),
                challenge.getStarRating(),
                userCount,
                challenge.getStatus().toString(),
                TagResponse.listOf(challenge.getChallengeTags().getTags()),
                challenge.getExamplePhotoUrls()
        );
    }
}
