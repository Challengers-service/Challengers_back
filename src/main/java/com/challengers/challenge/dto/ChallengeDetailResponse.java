package com.challengers.challenge.dto;

import com.challengers.challenge.domain.Challenge;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeDetailResponse {
    private Long challengeId;
    private String hostProfileImageUrl;
    private String hostName;
    private String imageUrl;
    private String examplePhotoDescription;

    public static ChallengeDetailResponse of(Challenge challenge) {
        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getHost().getImage(),
                challenge.getHost().getName(),
                challenge.getImageUrl(),
                challenge.getChallengePhotoDescription()
        );
    }
}
