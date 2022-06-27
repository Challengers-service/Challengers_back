package com.challengers.challengephoto.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.challengephoto.dto.ChallengePhotoRequest;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.AwsS3Uploader;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChallengePhotoService {
    private final ChallengePhotoRepository challengePhotoRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public void upload(ChallengePhotoRequest challengePhotoRequest, Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        String photoUrl = awsS3Uploader.uploadImage(challengePhotoRequest.getPhoto());
        challengePhotoRepository.save(ChallengePhoto.create(challenge,user,photoUrl));
    }
}
