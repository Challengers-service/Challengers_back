package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.tag.dto.TagResponse;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ChallengeDetailResponse {
    private Long id;

    private Long hostId;
    private String hostProfileImageUrl;
    private String hostName;

    private String name;
    private String challengeRule;
    private CheckFrequencyType checkFrequencyType;
    private Integer checkTimesPerRound;
    private String category;
    private String startDate;
    private String endDate;
    private int depositPoint;
    private String introduction;
    private int userCountLimit;
    private String status;
    private List<TagResponse> tags;
    private List<String> examplePhotos;
    private String createdDate;

    private int userCount;
    private Float starRating;
    private int reviewCount;
    private boolean cart;
    private boolean hasJoined;
    private long expectedReward;

    public static ChallengeDetailResponse of(Challenge challenge, int userCount, float starRating, int reviewCount,
                                             boolean cart, boolean hasJoined, long expectedReward) {
        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getHost().getId(),
                challenge.getHost().getImage(),
                challenge.getHost().getName(),
                challenge.getName(),
                challenge.getChallengeRule(),
                challenge.getCheckFrequencyType(),
                challenge.getCheckTimesPerRound(),
                challenge.getCategory().toString(),
                challenge.getStartDate().toString(),
                challenge.getEndDate().toString(),
                challenge.getDepositPoint(),
                challenge.getIntroduction(),
                challenge.getUserCountLimit(),
                challenge.getStatus().toString(),
                TagResponse.listOf(challenge.getChallengeTags().getTags()),
                challenge.getExamplePhotoUrls(),
                challenge.getCreatedDate().toLocalDate().toString(),
                userCount,
                starRating,
                reviewCount,
                cart,
                hasJoined,
                expectedReward
        );
    }
}
