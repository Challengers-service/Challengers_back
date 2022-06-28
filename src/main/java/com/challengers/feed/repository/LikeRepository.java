package com.challengers.feed.repository;

import com.challengers.feed.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndChallengePhotoId(Long userId, Long challengePhotoId);
}
