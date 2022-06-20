package com.challengers.review.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.review.domain.Review;
import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.repository.ReviewRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ChallengeRepository challengeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public void create(ReviewRequest reviewRequest, Long userId) {
        Challenge challenge = challengeRepository.findById(reviewRequest.getChallengeId()).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        Review review = Review.builder()
                .challenge(challenge)
                .user(user)
                .title(reviewRequest.getTitle())
                .content(reviewRequest.getContent())
                .starRating(reviewRequest.getStarRating())
                .build();

        reviewRepository.save(review);
    }

    public List<ReviewResponse> findReviews(Long challengeId) {
        return ReviewResponse.listOf(reviewRepository.findAllByChallengeId(challengeId));
    }

    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoSuchElementException::new);
        if (!review.getUser().getId().equals(userId)) throw new RuntimeException("권한이 없습니다.");

        reviewRepository.delete(review);
    }
}
