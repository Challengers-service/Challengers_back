package com.challengers.photocheck.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.common.AwsS3Uploader;
import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.challengers.photocheck.dto.CheckRequest;
import com.challengers.photocheck.dto.PhotoCheckRequest;
import com.challengers.photocheck.repository.PhotoCheckRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoCheckServiceTest {
    @Mock AwsS3Uploader awsS3Uploader;
    @Mock UserRepository userRepository;
    @Mock ChallengeRepository challengeRepository;
    @Mock ChallengePhotoRepository challengePhotoRepository;
    @Mock UserChallengeRepository userChallengeRepository;
    @Mock PhotoCheckRepository photoCheckRepository;

    PhotoCheckService photoCheckService;
    User user;
    Challenge challenge;
    UserChallenge userChallenge;
    PhotoCheckRequest photoCheckRequest;
    PhotoCheck photoCheck;

    @BeforeEach
    void setUp() {
        photoCheckService = new PhotoCheckService(awsS3Uploader,
                userRepository,
                challengeRepository,
                challengePhotoRepository,
                userChallengeRepository,
                photoCheckRepository);

        user = User.builder()
                .id(1L)
                .build();

        challenge = Challenge.builder()
                .id(1L)
                .host(user)
                .status(ChallengeStatus.IN_PROGRESS)
                .checkTimesPerRound(1)
                .build();

        userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .status(UserChallengeStatus.IN_PROGRESS)
                .build();

        photoCheckRequest = new PhotoCheckRequest(challenge.getId(),
                new MockMultipartFile("asdf","asf".getBytes(StandardCharsets.UTF_8)));

        photoCheck = PhotoCheck.builder()
                .status(PhotoCheckStatus.WAITING)
                .userChallenge(userChallenge)
                .build();
    }

    @Test
    @DisplayName("인증샷 등록")
    void addPhotoCheck() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any())).thenReturn(Optional.of(userChallenge));
        when(photoCheckRepository.countByUserChallengeIdAndRound(any(),any())).thenReturn(0L);
        when(awsS3Uploader.uploadImage(any())).thenReturn("https://tempPhotoUrl.png");

        photoCheckService.addPhotoCheck(photoCheckRequest, 1L);

        verify(challengePhotoRepository).save(any());
        verify(photoCheckRepository).save(any());
    }

    @Test
    @DisplayName("인증샷 등록 실패 - 진행중인 챌린지가 아닙니다.")
    void addPhotoCheck_fail_challenge_status_not_IN_PROGRESS() {
        Challenge challenge2 = Challenge.builder().status(ChallengeStatus.READY).build();
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge2));

        Assertions.assertThatThrownBy(()->photoCheckService.addPhotoCheck(photoCheckRequest, 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 등록 실패 - 해당 챌린지에 참여중이 아닙니다.")
    void addPhotoCheck_fail_user_challenge_status_not_IN_PROGRESS() {
        UserChallenge userChallenge2 = UserChallenge.builder().status(UserChallengeStatus.FAIL).build();
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any())).thenReturn(Optional.of(userChallenge2));

        Assertions.assertThatThrownBy(()->photoCheckService.addPhotoCheck(photoCheckRequest, 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 등록 실패 - 해당 회차에 인증 사진을 전부 올렸습니다.")
    void addPhotoCheck_fail_full() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any())).thenReturn(Optional.of(userChallenge));
        when(photoCheckRepository.countByUserChallengeIdAndRound(any(),any())).thenReturn(1L);

        Assertions.assertThatThrownBy(()->photoCheckService.addPhotoCheck(photoCheckRequest, 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 통과")
    void passPhotoCheck() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));

        photoCheckService.passPhotoCheck(checkRequest, 1L);

        Assertions.assertThat(photoCheck.getStatus()).isEqualTo(PhotoCheckStatus.PASS);
    }

    @Test
    @DisplayName("인증샷 통과 실패 - 인증샷을 처리할 권한이 없습니다.")
    void passPhotoCheck_fail_unauthorized() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));

        Assertions.assertThatThrownBy(()->photoCheckService.passPhotoCheck(checkRequest, 2L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 통과 실패 - 이미 인증 통과된 사진이 있습니다.")
    void passPhotoCheck_fail() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L)));
        PhotoCheck photoCheck = PhotoCheck.builder().status(PhotoCheckStatus.PASS).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));


        Assertions.assertThatThrownBy(()->photoCheckService.passPhotoCheck(checkRequest, 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 실패 처리")
    void failPhotoCheck() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));

        photoCheckService.failPhotoCheck(checkRequest, 1L);

        Assertions.assertThat(photoCheck.getStatus()).isEqualTo(PhotoCheckStatus.FAIL);
    }

    @Test
    @DisplayName("인증샷 통과 실패 - 인증샷을 처리할 권한이 없습니다.")
    void failPhotoCheck_fail_unauthorized() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));

        Assertions.assertThatThrownBy(()->photoCheckService.failPhotoCheck(checkRequest, 2L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증샷 통과 실패 - 이미 인증 실패 처리된 사진이 있습니다.")
    void failPhotoCheck_fail() {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L)));
        PhotoCheck photoCheck = PhotoCheck.builder().status(PhotoCheckStatus.FAIL).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(photoCheckRepository.findById(any())).thenReturn(Optional.of(photoCheck));


        Assertions.assertThatThrownBy(()->photoCheckService.failPhotoCheck(checkRequest, 1L))
                .isInstanceOf(RuntimeException.class);
    }
}