package com.challengers.review.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.review.domain.Review;
import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.dto.ReviewUpdateRequest;
import com.challengers.review.repository.ReviewRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ChallengeRepository challengeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(ReviewRequest reviewRequest, Long userId) {
        Challenge challenge = challengeRepository.findById(reviewRequest.getChallengeId())
                .orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        if (reviewRepository.findByChallengeIdAndUserId(challenge.getId(), userId).isPresent())
            throw new RuntimeException("한 챌린지에 하나의 리뷰만 생성할 수 있습니다.");

        Review review = Review.builder()
                .challenge(challenge)
                .user(user)
                .title(reviewRequest.getTitle())
                .content(reviewRequest.getContent())
                .starRating(reviewRequest.getStarRating())
                .build();

        challenge.addReviewRelation(review.getStarRating());
        reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoSuchElementException::new);
        authorization(review.getUser().getId(), userId);

        review.getChallenge().deleteReviewRelation(review.getStarRating());
        reviewRepository.delete(review);
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoSuchElementException::new);
        authorization(review.getUser().getId(), userId);
        review.update(reviewUpdateRequest);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findReviews(Long challengeId) {
        return ReviewResponse.listOf(reviewRepository.findAllByChallengeId(challengeId));
    }

    private void authorization(Long authorizationId, Long userId) {
        if (!authorizationId.equals(userId)) throw new RuntimeException("권한이 없습니다.");
    }
}
