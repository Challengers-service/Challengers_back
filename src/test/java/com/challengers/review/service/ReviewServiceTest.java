package com.challengers.review.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.review.domain.Review;
import com.challengers.review.dto.ReviewRequest;
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

import static org.junit.jupiter.api.Assertions.*;
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
                .build();

        review = Review.builder()
                .id(1L)
                .user(user)
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
    @DisplayName("리뷰 삭제 성공")
    void delete() {
        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        reviewService.delete(1L,review.getUser().getId());

        verify(reviewRepository).delete(any());
    }
}