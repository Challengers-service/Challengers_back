package com.challengers.photocheck.dto;

import com.challengers.photocheck.domain.PhotoCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoCheckResponse {
    private Long id;

    private Long userChallengeId;

    private Long challengePhotoId;

    private int round;

    private String status;

    public static PhotoCheckResponse of(PhotoCheck photoCheck) {
        return new PhotoCheckResponse(photoCheck.getId(),
                photoCheck.getUserChallenge().getId(),
                photoCheck.getChallengePhoto().getId(),
                photoCheck.getRound(),
                photoCheck.getStatus().toString());
    }
}
