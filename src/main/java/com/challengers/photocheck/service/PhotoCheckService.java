package com.challengers.photocheck.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.AwsS3Uploader;
import com.challengers.common.exception.NotFoundException;
import com.challengers.common.exception.UnAuthorizedException;
import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.challengers.photocheck.dto.CheckRequest;
import com.challengers.photocheck.dto.PhotoCheckRequest;
import com.challengers.photocheck.dto.PhotoCheckResponse;
import com.challengers.photocheck.repository.PhotoCheckRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PhotoCheckService {
    private final AwsS3Uploader awsS3Uploader;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengePhotoRepository challengePhotoRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final PhotoCheckRepository photoCheckRepository;

    @Transactional(readOnly = true)
    public PhotoCheckResponse findPhotoCheck(Long photoCheckId) {
        return PhotoCheckResponse.of(photoCheckRepository
                .findById(photoCheckId).orElseThrow(NotFoundException::new));
    }

    @Transactional
    public Long addPhotoCheck(PhotoCheckRequest photoCheckRequest, Long userId) {
        Challenge challenge = challengeRepository.findById(photoCheckRequest.getChallengeId())
                .orElseThrow(NoSuchElementException::new);

        if (!challenge.getStatus().equals(ChallengeStatus.IN_PROGRESS))
            throw new IllegalStateException("진행중인 챌린지가 아닙니다.");
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userId, challenge.getId())
                .orElseThrow(() -> new IllegalStateException("해당 챌린지를 참여하고 있지 않습니다."));

        if(!userChallenge.getStatus().equals(UserChallengeStatus.IN_PROGRESS))
            throw new IllegalStateException("해당 챌린지에 성공하거나 실패하여 현재 진행하고 있는 상태가 아닙니다.");

        if (photoCheckRepository.countByUserChallengeIdAndRound(challenge.getId(), challenge.getRound())
                >= challenge.getCheckTimesPerRound())
            throw new RuntimeException("이미 해당 회차에 인증 사진을 전부 올렸습니다.");

        java.lang.String photoUrl = awsS3Uploader.uploadImage(photoCheckRequest.getPhoto());
        ChallengePhoto challengePhoto = ChallengePhoto.builder()
                .challenge(challenge)
                .user(user)
                .photoUrl(photoUrl)
                .build();
        challengePhotoRepository.save(challengePhoto);

        PhotoCheck photoCheck = PhotoCheck.builder()
                .userChallenge(userChallenge)
                .challengePhoto(challengePhoto)
                .round(challenge.getRound())
                .status(PhotoCheckStatus.WAITING)
                .build();
        photoCheckRepository.save(photoCheck);

        return photoCheck.getId();
    }

    @Transactional
    public void updatePhotoCheckStatus(CheckRequest checkRequest, Long userId, PhotoCheckStatus photoCheckStatus) {
        Challenge challenge = photoCheckRepository.findById(checkRequest.getPhotoCheckIds().get(0))
                .orElseThrow(NoSuchElementException::new)
                .getUserChallenge().getChallenge();

        if (!challenge.getHost().getId().equals(userId))
            throw new UnAuthorizedException("인증샷을 처리할 권한이 없습니다.");

        photoCheckRepository.updateStatusByIds(checkRequest.getPhotoCheckIds(), photoCheckStatus);
    }

}
