package com.challengers.review.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.common.exception.NotFoundException;
import com.challengers.common.exception.UnAuthorizedException;
import com.challengers.review.domain.Review;
import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.dto.ReviewUpdateRequest;
import com.challengers.review.repository.ReviewRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalStateException("이미 작성한 리뷰가 있습니다. 한 챌린지에 하나의 리뷰만 생성할 수 있습니다.");

        Review review = Review.builder()
                .challenge(challenge)
                .user(user)
                .title(reviewRequest.getTitle())
                .content(reviewRequest.getContent())
                .starRating(reviewRequest.getStarRating())
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        authorization(review.getUser().getId(), userId);
        
        reviewRepository.delete(review);
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        authorization(review.getUser().getId(), userId);
        review.update(reviewUpdateRequest);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> findReviews(Pageable pageable, Long challengeId) {
        return reviewRepository.findAllByChallengeId(pageable, challengeId).map(ReviewResponse::of);
    }

    private void authorization(Long authorizationId, Long userId) {
        if (!authorizationId.equals(userId)) throw new UnAuthorizedException("권한이 없습니다.");
    }
}
