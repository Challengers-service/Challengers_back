package com.challengers.review.repository;

import com.challengers.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByChallengeId(Long challengeId);
    Optional<Review> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
