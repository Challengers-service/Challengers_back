package com.challengers.challenge.service;

import com.challengers.cart.repository.CartRepository;
import com.challengers.challenge.domain.Category;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.dto.ChallengeUpdateRequest;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.common.AwsS3Uploader;
import com.challengers.examplephoto.repository.ExamplePhotoRepository;
import com.challengers.tag.domain.Tag;
import com.challengers.tag.repository.TagRepository;
import com.challengers.user.domain.AuthProvider;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.domain.UserChallenge;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {
    @Mock UserRepository userRepository;
    @Mock ChallengeRepository challengeRepository;
    @Mock ExamplePhotoRepository examplePhotoRepository;
    @Mock TagRepository tagRepository;
    @Mock UserChallengeRepository userChallengeRepository;
    @Mock AchievementRepository achievementRepository;
    @Mock AwsS3Uploader awsS3Uploader;
    @Mock CartRepository cartRepository;

    ChallengeService challengeService;

    ChallengeRequest challengeRequest;
    User user;
    Challenge challenge;

    @BeforeEach
    void setUp() {
        challengeService = new ChallengeService(challengeRepository,achievementRepository,tagRepository,
                userRepository,examplePhotoRepository,userChallengeRepository,awsS3Uploader,cartRepository);

        user = User.builder()
                .id(0L)
                .name("테스터")
                .email("test@gmail.com")
                .image("https:/asfawfasfas.png")
                .bio("my bio")
                .password(null)
                .role(Role.USER)
                .visitTime(LocalDate.now())
                .attendanceCount(0L)
                .challengeCount(0L)
                .provider(AuthProvider.google)
                .providerId("12412521")
                .build();

        challengeRequest = ChallengeRequest.builder()
                .name("미라클 모닝 - 아침 7시 기상")
                .image(new MockMultipartFile("테스트사진.png","테스트사진.png","image/png","saf".getBytes()))
                .challengeRule("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .checkFrequencyType("EVERY_DAY")
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .depositPoint(1000)
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("예시사진1.png","예시사진1.png","image/png","asgas".getBytes()),
                        new MockMultipartFile("예시사진2.png","예시사진2.png","image/png","asgasagagas".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("미라클모닝", "기상")))
                .build();

        challenge = Challenge.builder()
                .id(1L)
                .host(user)
                .name("미라클 모닝 - 아침 7시 기상")
                .imageUrl("https://imageUrl.png")
                .challengeRule("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .category(Category.LIFE)
                .starRating(3.5f)
                .depositPoint(1000)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .userCountLimit(2000)
                .build();
    }

    @Test
    @DisplayName("챌린지 개설 성공")
    void create() {
        //given
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(awsS3Uploader.uploadImage(any()))
                .thenReturn("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e2bce0.PNG");
        when(tagRepository.findTagByName(any())).thenReturn(Optional.of(new Tag("임시 태그")));

        //when
        challengeService.create(challengeRequest, user.getId());

        //then
        verify(challengeRepository).save(any());
    }

    @Test
    @DisplayName("챌린지 대표 이미지, 소개글 수정 성공")
    void update_all() {
        //given
        String updatedImageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e20.PNG";
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                new MockMultipartFile("image","image".getBytes()), "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(awsS3Uploader.uploadImage(any())).thenReturn(updatedImageUrl);


        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHost().getId());

        //then
        assertThat(challenge.getImageUrl()).isEqualTo(updatedImageUrl);
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("챌린지 소개글 수정 성공")
    void update_introduction() {
        //given
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHost().getId());

        //then
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("챌린지 수정 실패 - 권한이 없습니다.")
    void update_unauthorized() {
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        assertThatThrownBy(()-> challengeService.update(challengeUpdateRequest, challenge.getId(),
                challenge.getHost().getId()+1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 삭제 성공")
    void delete() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(1L);
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,user)));

        challengeService.delete(challenge.getId(),user.getId());

        verify(challengeRepository).delete(any());
    }


    @Test
    @DisplayName("챌린지 삭제 실패 - 참가자가 2명 이상일 경우")
    void delete_fail_proceeding() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(2L);

        assertThatThrownBy(() -> challengeService.delete(challenge.getId(),user.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 참여 성공")
    void join() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        challengeService.join(1L,1L);

        verify(userChallengeRepository).save(any());
    }

    @Test
    @DisplayName("챌린지 참여 실패 - 참여 인원이 초과되는 경우")
    void join_failed_due_to_overcrowding() {
        Challenge challengeFull = Challenge.builder()
                .id(1L)
                .userCount(3)
                .userCountLimit(3)
                .build();
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challengeFull));

        assertThatThrownBy(()->challengeService.join(1L,1L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 참여 실패 - 이미 해당 챌린지에 참여하고 있는 경우")
    void join_failed_because_already_participating_in_the_challenge() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,user)));

        assertThatThrownBy(()->challengeService.join(1L,1L)).isInstanceOf(RuntimeException.class);
    }
}
