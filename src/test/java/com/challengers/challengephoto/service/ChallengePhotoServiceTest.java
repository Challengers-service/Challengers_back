package com.challengers.challengephoto.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.dto.ChallengePhotoRequest;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.AwsS3Uploader;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengePhotoServiceTest {
    @Mock private ChallengePhotoRepository challengePhotoRepository;
    @Mock private ChallengeRepository challengeRepository;
    @Mock private UserRepository userRepository;
    @Mock private AwsS3Uploader awsS3Uploader;

    private ChallengePhotoService challengePhotoService;
    private Challenge challenge;
    private User user;
    private ChallengePhotoRequest challengePhotoRequest;

    @BeforeEach
    void setUp() {
        challengePhotoService = new ChallengePhotoService(
                challengePhotoRepository,
                challengeRepository,
                userRepository,
                awsS3Uploader);

        challenge = Challenge.builder().build();

        user = User.builder().build();

        challengePhotoRequest = new ChallengePhotoRequest(
                new MockMultipartFile("tempName","multipartFile content".getBytes()));
    }

    @Test
    @DisplayName("인증샷 업로드 성공")
    void upload() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(awsS3Uploader.uploadImage(any())).thenReturn("https://tempPhotoUrl.png");

        challengePhotoService.upload(challengePhotoRequest,1L,1L);

        verify(challengePhotoRepository).save(any());
    }

}