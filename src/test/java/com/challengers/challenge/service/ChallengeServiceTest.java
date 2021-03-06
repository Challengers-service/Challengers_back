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
                .name("?????????")
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
                .name("????????? ?????? - ?????? 7??? ??????")
                .image(new MockMultipartFile("???????????????.png","???????????????.png","image/png","saf".getBytes()))
                .challengeRule("7?????? ???????????? ????????? ????????? ?????? ????????? ????????? ???????????? ?????????.")
                .checkFrequencyType("EVERY_DAY")
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .depositPoint(1000)
                .introduction("?????? ?????? 7?????? ???????????? ????????? ???????????????.")
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("????????????1.png","????????????1.png","image/png","asgas".getBytes()),
                        new MockMultipartFile("????????????2.png","????????????2.png","image/png","asgasagagas".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("???????????????", "??????")))
                .build();

        challenge = Challenge.builder()
                .id(1L)
                .host(user)
                .name("????????? ?????? - ?????? 7??? ??????")
                .imageUrl("https://imageUrl.png")
                .challengeRule("7?????? ???????????? ????????? ????????? ?????? ????????? ????????? ???????????? ?????????.")
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .category(Category.LIFE)
                .starRating(3.5f)
                .depositPoint(1000)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .introduction("?????? ?????? 7?????? ???????????? ????????? ???????????????.")
                .userCountLimit(2000)
                .build();
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void create() {
        //given
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(awsS3Uploader.uploadImage(any()))
                .thenReturn("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e2bce0.PNG");
        when(tagRepository.findTagByName(any())).thenReturn(Optional.of(new Tag("?????? ??????")));

        //when
        challengeService.create(challengeRequest, user.getId());

        //then
        verify(challengeRepository).save(any());
    }

    @Test
    @DisplayName("????????? ?????? ?????????, ????????? ?????? ??????")
    void update_all() {
        //given
        String updatedImageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e20.PNG";
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                new MockMultipartFile("image","image".getBytes()), "????????? ????????? ????????? ?????????.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(awsS3Uploader.uploadImage(any())).thenReturn(updatedImageUrl);


        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHost().getId());

        //then
        assertThat(challenge.getImageUrl()).isEqualTo(updatedImageUrl);
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("????????? ????????? ?????? ??????")
    void update_introduction() {
        //given
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "????????? ????????? ????????? ?????????.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHost().getId());

        //then
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ????????? ????????????.")
    void update_unauthorized() {
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "????????? ????????? ????????? ?????????.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        assertThatThrownBy(()-> challengeService.update(challengeUpdateRequest, challenge.getId(),
                challenge.getHost().getId()+1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void delete() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(1L);
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,user)));

        challengeService.delete(challenge.getId(),user.getId());

        verify(challengeRepository).delete(any());
    }


    @Test
    @DisplayName("????????? ?????? ?????? - ???????????? 2??? ????????? ??????")
    void delete_fail_proceeding() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(2L);

        assertThatThrownBy(() -> challengeService.delete(challenge.getId(),user.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void join() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        challengeService.join(1L,1L);

        verify(userChallengeRepository).save(any());
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ?????? ????????? ???????????? ??????")
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
    @DisplayName("????????? ?????? ?????? - ?????? ?????? ???????????? ???????????? ?????? ??????")
    void join_failed_because_already_participating_in_the_challenge() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,user)));

        assertThatThrownBy(()->challengeService.join(1L,1L)).isInstanceOf(RuntimeException.class);
    }
}
