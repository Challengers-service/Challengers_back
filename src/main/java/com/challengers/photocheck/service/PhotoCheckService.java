package com.challengers.photocheck.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.AwsS3Uploader;
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
                .findById(photoCheckId).orElseThrow(NoSuchElementException::new));
    }

    @Transactional
    public Long addPhotoCheck(PhotoCheckRequest photoCheckRequest, Long userId) {
        Challenge challenge = challengeRepository.findById(photoCheckRequest.getChallengeId()).orElseThrow(NoSuchElementException::new);
        if (!challenge.getStatus().equals(ChallengeStatus.IN_PROGRESS))
            throw new RuntimeException("???????????? ???????????? ????????????.");
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userId, challenge.getId())
                .orElseThrow(NoSuchElementException::new);

        if(!userChallenge.getStatus().equals(UserChallengeStatus.IN_PROGRESS))
            throw new RuntimeException("?????? ???????????? ???????????? ????????????.");

        if (photoCheckRepository.countByUserChallengeIdAndRound(challenge.getId(), challenge.getRound())
                >= challenge.getCheckTimesPerRound())
            throw new RuntimeException("?????? ?????? ????????? ?????? ????????? ?????? ???????????????.");

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

    // TODO : Bulk Update ????????????
    @Transactional
    public void passPhotoCheck(CheckRequest checkRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        User host = photoCheckRepository.findById(checkRequest.getPhotoCheckIds().get(0))
                .orElseThrow(NoSuchElementException::new)
                .getUserChallenge().getChallenge().getHost();
        if (!host.getId().equals(userId))
            throw new RuntimeException("???????????? ????????? ????????? ????????????.");

        for (Long photoCheckId : checkRequest.getPhotoCheckIds()) {
            PhotoCheck photoCheck = photoCheckRepository.findById(photoCheckId)
                    .orElseThrow(NoSuchElementException::new);
            if (photoCheck.getStatus().equals(PhotoCheckStatus.PASS))
                throw new RuntimeException("?????? ?????? ????????? ????????? ????????????.");
            photoCheck.pass();
        }
    }

    // TODO : Bulk Update ????????????
    @Transactional
    public void failPhotoCheck(CheckRequest checkRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        User host = photoCheckRepository.findById(checkRequest.getPhotoCheckIds().get(0))
                .orElseThrow(NoSuchElementException::new)
                .getUserChallenge().getChallenge().getHost();
        if (!host.getId().equals(userId))
            throw new RuntimeException("???????????? ????????? ????????? ????????????.");

        for (Long photoCheckId : checkRequest.getPhotoCheckIds()) {
            PhotoCheck photoCheck = photoCheckRepository.findById(photoCheckId)
                    .orElseThrow(NoSuchElementException::new);
            if (photoCheck.getStatus().equals(PhotoCheckStatus.FAIL))
                throw new RuntimeException("?????? ?????? ?????? ????????? ????????? ????????????.");
            photoCheck.fail();
        }
    }

}
