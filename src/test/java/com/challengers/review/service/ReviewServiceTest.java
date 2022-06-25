package com.challengers.review.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.review.domain.Review;
import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewUpdateRequest;
import com.challengers.review.repository.ReviewRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock ChallengeRepository challengeRepository;
    @Mock ReviewRepository reviewRepository;
    @Mock UserRepository userRepository;
    ReviewService reviewService;

    User user;
    Challenge challenge;
    Review review;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(challengeRepository,reviewRepository,userRepository);

        user = User.builder()
                .id(1L)
                .build();

        challenge = Challenge.builder()
                .id(1L)
                .totalStarRating(13.5f)
                .starRating(3.4f)
                .reviewCount(4)
                .build();

        review = Review.builder()
                .id(1L)
                .user(user)
                .challenge(challenge)
                .title("리뷰 제목입니다.")
                .content("리뷰 내용입니다.")
                .starRating(5.0f)
                .build();
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void create() {
        ReviewRequest reviewRequest = new ReviewRequest(challenge.getId(),"title",
                "content",3.4f);
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        reviewService.create(reviewRequest,user.getId());

        verify(reviewRepository).save(any());
    }

    @Test
    @DisplayName("리뷰 생성 실패 - 이미 작성한 리뷰가 있는 경우")
    void create_failed_because_there_are_other_reviews_already_written() {
        ReviewRequest reviewRequest = new ReviewRequest(challenge.getId(),"title",
                "content",3.4f);
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(reviewRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reviewService.create(reviewRequest,user.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete() {
        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        reviewService.delete(1L,review.getUser().getId());

        verify(reviewRepository).delete(any());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void update() {
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("수정할 리뷰 제목입니다.",
                "수정할 리뷰 내용입니다.", 3.2f);
        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        reviewService.update(1L,reviewUpdateRequest,review.getUser().getId());

        assertThat(review.getTitle()).isEqualTo(reviewUpdateRequest.getTitle());
        assertThat(review.getContent()).isEqualTo(reviewUpdateRequest.getContent());
        assertThat(review.getStarRating()).isEqualTo(reviewUpdateRequest.getStarRating());
    }
}